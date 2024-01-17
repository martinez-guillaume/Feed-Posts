package com.devid_academy.tutocomposeoct23.ui.main

import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.Screen
import com.devid_academy.tutocomposeoct23.network.ArticleDto
import com.devid_academy.tutocomposeoct23.theme.TutoComposeOct23Theme
import com.devid_academy.tutocomposeoct23.ui.splash.SplashViewModel
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt


@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel) {


    val articlesList by viewModel.articleListFilteredStateFlow.collectAsState()
    val selectedCategory by viewModel.selectedCategoryIdStateFlow.collectAsState()
    val articleDeletionResult by viewModel.articleDeletionResult.collectAsState(null)

    viewModel.loadArticles()

    MainContent(
        goToCrea = {
            navController.navigate(Screen.Crea.route)
        },
        goToLogin = {
            viewModel.logoutUser()
            navController.navigate(Screen.Login.route)
        },
        currentUserId = viewModel.currentUserId,

        goToEdit = { articleId ->
            val articleDto = articlesList.find { it.id == articleId }
            articleDto?.let {
                viewModel.getJsonFromArticle(it)
                val articleJson = viewModel.currentArticleJson.value
                val encodedArticleJson = Uri.encode(articleJson)
                navController.navigate("${Screen.Edit.route}/$encodedArticleJson")
            }
        },
        onView = { _ -> },

        onDeleteArticle = { articleId ->
            viewModel.viewModelScope.launch {
                viewModel.deleteArticle(articleId, viewModel.getToken())
            }
        },


        articles = articlesList,
        selectedCategory = selectedCategory,
        onRadioButtonSelect = { viewModel.updateFilteredArticles(it) },
    )
}


@Composable
fun MainContent(
    goToCrea: () -> Unit,
    goToLogin: () -> Unit,
    currentUserId: Long,
    goToEdit: (Long) -> Unit,
    onView: (Int) -> Unit,
    articles: List<ArticleDto>,
    selectedCategory: Int,
    onRadioButtonSelect: (Int) -> Unit,
    onDeleteArticle: (Long) -> Unit
) {
    val categoriesOptions = remember {
        listOf(
            "Tout" to 0,
            "Sport" to 1,
            "Manga" to 2,
            "Diverse" to 3
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier.clickable(onClick = goToCrea),
                painter = painterResource(android.R.drawable.ic_input_add),
                contentDescription = "image pour aller sur le page créer un article"

            )
            Image(
                modifier = Modifier.clickable(onClick = goToLogin),
                painter = painterResource(android.R.drawable.ic_lock_power_off),
                contentDescription = "image pour se déconnecter"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(articles) { article ->
                ArticleItem(
                    article = article,
                    currentUserId = currentUserId,
                    onEditArticle = { articleId ->
                        goToEdit(articleId)
                    },
                    onViewArticle = { onView(article.id.toInt()) },
                    onDeleteArticle = onDeleteArticle
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            categoriesOptions.forEach { it ->

                val isSelected = (selectedCategory == it.second)

                RadioButton(
                    selected = (selectedCategory == it.second),
                    onClick = {
                        Log.d(
                            "RadioButton",
                            "Clicked on RadioButton for category: ${it.first}, isSelected: $isSelected"
                        )
                        onRadioButtonSelect(it.second)
                    },
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = it.first,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}


@Composable
fun formatArticleDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return try {
        val date = inputFormat.parse(dateString)
        if (date != null) outputFormat.format(date) else ""
    } catch (e: Exception) {
        ""
    }
}


@Composable
fun getCategoryName(categoryId: Int): String {
    return when (categoryId) {
        1 -> "Sport"
        2 -> "Manga"
        3 -> "Divers"
        else -> "Tout"
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleItem(

    article: ArticleDto,
    currentUserId: Long,
    onEditArticle: (Long) -> Unit,
    onViewArticle: () -> Unit,
    onDeleteArticle: (Long) -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    var isImageExpanded by remember { mutableStateOf(false) }
    var isDeleted by remember { mutableStateOf(false) }
    val sizePx = with(LocalDensity.current) { 200.dp.toPx() }
    val halfSwipe = -sizePx / 3
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val anchors = mapOf(0f to 0, halfSwipe to 1, -sizePx to 2)


    val backgroundColor = when (article.categorie) {
        1 -> Color.Green
        2 -> Color.Yellow
        3 -> Color.LightGray
        else -> Color.Gray
    }



    LaunchedEffect(swipeableState.currentValue) {
        Log.d(
            "ArticleItem",
            "currentUserId: $currentUserId, article.id_u: ${article.id_u}, swipeableState.currentValue: ${swipeableState.currentValue}"
        )
        if (!isDeleted && currentUserId == article.id_u && swipeableState.currentValue == 2) {
            Log.d("ArticleItem", "Suppression de l'article ${article.id}")
            onDeleteArticle(article.id)
            Toast.makeText(context, "Annonce supprimée", Toast.LENGTH_SHORT).show()
            isDeleted = true
            delay(1000L)
            swipeableState.snapTo(0)
        }
    }


    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = currentUserId == article.id_u,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .height(90.dp)
                    .width(330.dp)
                    .background(Color.Red)
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_24),
                    contentDescription = "Supprimer",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .clickable {
                            if (!isDeleted) {
                                onDeleteArticle(article.id)
                                isDeleted = true
                            }
                        }
                )
            }
        }


        Card(
            backgroundColor = backgroundColor,
            modifier = Modifier
                .clickable {

                    if (currentUserId == article.id_u) {
                        onEditArticle(article.id)
                    } else {
                        onViewArticle()
                        isExpanded = !isExpanded
                        isImageExpanded = !isImageExpanded
                    }
                }
                .fillMaxWidth()
                .padding(10.dp)
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .then(
                    if (currentUserId == article.id_u) {
                        Modifier.swipeable(
                            state = swipeableState,
                            anchors = anchors,
                            thresholds = { _, _ -> FractionalThreshold(0.3f) },
                            orientation = Orientation.Horizontal
                        )
                    } else {
                        Modifier
                    }
                )
                .animateContentSize(),
            elevation = 5.dp,
            shape = RoundedCornerShape(10.dp)
        ) {
            Box {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        val painter = rememberAsyncImagePainter(          // image par défaut
                            model = article.url_image,
                            error = painterResource(R.drawable.feedarticles_logo)
                        )

                        Image(
                            painter = painter,
                            contentDescription = "images des petites annonces",
                            contentScale = ContentScale.Crop,  // pour que l'image prenne tout le cercle
                            modifier = Modifier
                                .size(if (isImageExpanded) 105.dp else 60.dp)
                                .clip(CircleShape)
                                .animateContentSize()

                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = article.titre,
                            color = Color(0xFF0099CC),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(0.1f))
                        AnimatedVisibility(visible = isExpanded) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_expand_less_24),
                                contentDescription = "Indicateur d'expansion",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }



                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Du : ${formatArticleDate(article.created_at)}")
                                Text(text = "Cat. ${getCategoryName(article.categorie)}")
                            }
                            Text(text = article.descriptif)
                        }
                    }
                }
            }
        }
    }
}


