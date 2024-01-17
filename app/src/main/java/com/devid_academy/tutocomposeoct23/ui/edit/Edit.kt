package com.devid_academy.tutocomposeoct23.ui.edit

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.devid_academy.tutocomposeoct23.theme.TutoComposeOct23Theme
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devid_academy.tutocomposeoct23.R
import androidx.compose.material.RadioButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.devid_academy.tutocomposeoct23.Screen
import com.devid_academy.tutocomposeoct23.network.ArticleDto


@Composable
fun EditScreen(
    navController: NavController,
    viewModel: EditViewModel,
    articleDtoJson: String,
) {
    val context = LocalContext.current

    viewModel.getArticleFromJson(articleDtoJson)

    val article by viewModel.dataStateFlow.collectAsState()

    EditContent(
        article,
        onClickEditButton = { articleId, title, content, picture, selectedCategory ->
            viewModel.editArticle(articleId, title, content, picture, selectedCategory)
        },
    )
    LaunchedEffect("guiom") {
        viewModel.articleEditionResult.collect { result ->
            Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                navController.navigate(Screen.Main.route)

        }
    }
}

@Composable
fun EditContent(
    article: ArticleDto?,
    onClickEditButton:(Long, String, String, String, Int) -> Unit
) {

    var title by remember { mutableStateOf(article?.titre ?: "") }
    var content by remember { mutableStateOf(article?.descriptif ?: "") }
    var url by remember { mutableStateOf(article?.url_image ?: "") }
    var selectedCategory by remember { mutableStateOf(article?.categorie ?: 3) }
    val articleId = article?.id ?: 0L

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Edition Article",
            color = Color(0xFF0099CC),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = title,
            onValueChange = { title = it },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0099CC),
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color(0xFF0099CC),
                unfocusedLabelColor = Color(0xFF0099CC)
            )

        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier.height(120.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0099CC),
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color(0xFF0099CC),
                unfocusedLabelColor = Color(0xFF0099CC)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = url,
            onValueChange = { url = it },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0099CC),
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color(0xFF0099CC),
                unfocusedLabelColor = Color(0xFF0099CC)
            )

        )

        Spacer(modifier = Modifier.height(20.dp))


        Image(
            modifier = Modifier.size(width = 80.dp, height = 80.dp),
            painter = painterResource(R.drawable.feedarticles_logo),
            contentDescription = "image de l'annonce que l'on modifie"
        )

        Spacer(modifier = Modifier.height(40.dp))


        Row {
            RadioButton(
                selected = (selectedCategory == 1),
                onClick = { selectedCategory = 1 }
            )
            Text("Sport")

            RadioButton(
                selected = (selectedCategory == 2),
                onClick = { selectedCategory = 2 }
            )
            Text("Manga")

            RadioButton(
                selected = (selectedCategory == 3),
                onClick = { selectedCategory = 3 }
            )
            Text("Diverse")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(0.9f),
            onClick = {
                onClickEditButton(articleId, title, content, url, selectedCategory)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0099CC))
        ) {
            Text(
                text = "Mettre à jour",
                color = Color.White
            )
        }
    }
}

