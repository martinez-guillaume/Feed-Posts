package com.devid_academy.tutocomposeoct23.ui.crea

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.Screen
import com.devid_academy.tutocomposeoct23.theme.TutoComposeOct23Theme

@Composable
fun CreaScreen(navController: NavController, creaViewModel: CreaViewModel) {

    val context = LocalContext.current

    CreaContent(
        onClickCreateButton = { title, content, picture, selectedCategory ->
            creaViewModel.createArticle(title, content, picture, selectedCategory)
        }
    )

    LaunchedEffect(true) {
        creaViewModel.articleCreationResult.collect { result ->
            Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                navController.navigate(Screen.Main.route)

        }
    }
}


@Composable
fun CreaContent(
    modifier: Modifier = Modifier,
    onClickCreateButton: (String, String, String, Int) -> Unit

) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var picture by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(3) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Nouvel Article",
            color = Color(0xFF0099CC),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(50.dp))

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Titre") },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0099CC),
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color(0xFF0099CC),
                unfocusedLabelColor = Color(0xFF0099CC)
        )
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Contenu",
            color = Color(0xFF0099CC),
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            textAlign = TextAlign.Start
        )

        TextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier.height(100.dp),
            colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0xFF0099CC),
                    backgroundColor = Color.Transparent,
                    focusedLabelColor = Color(0xFF0099CC),
                    unfocusedLabelColor = Color(0xFF0099CC)
        )
        )

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = picture,
            onValueChange = { picture = it },
            label = { Text("Image URL") },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0099CC),
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color(0xFF0099CC),
                unfocusedLabelColor = Color(0xFF0099CC)
        )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            modifier = Modifier.size(width = 60.dp, height = 60.dp),
            painter = painterResource(R.drawable.feedarticles_logo),
            contentDescription = "image de l'annonce que l'on créer",

        )

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            RadioButton(
                selected = (selectedCategory == 1),
                onClick = { selectedCategory = 1 }
            )
            Text("Sport")

            Spacer(modifier = Modifier.width(8.dp))

            RadioButton(
                selected = (selectedCategory == 2),
                onClick = { selectedCategory = 2 }
            )
            Text("Manga")

            Spacer(modifier = Modifier.width(8.dp))

            RadioButton(
                selected = (selectedCategory == 3),
                onClick = { selectedCategory = 3 }
            )
            Text("Diverse")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                onClickCreateButton(title, content, picture, selectedCategory)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0099CC))
        ) {
            Text(
                text = "Enregistrer",
                color = Color.White
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreaPreview() {
    TutoComposeOct23Theme {
        CreaContent(
            onClickCreateButton = { _, _, _, _ ->
            }
        )
    }
}