package com.devid_academy.tutocomposeoct23.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devid_academy.tutocomposeoct23.theme.TutoComposeOct23Theme
import kotlinx.coroutines.delay
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.Screen



@Composable
fun SplashScreen(navController: NavController, splashViewModel: SplashViewModel) {

    SplashContent()

    LaunchedEffect("login") {
        splashViewModel.goToLogin.collect{
            navController.navigate(it){
                popUpTo(Screen.Splash.route) {
                    inclusive = true
                }
            }
        }
    }
    splashViewModel.getDirectionLoginAfterDelay()
}


@Composable
fun SplashContent(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0099CC)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.feedarticles_logo),
                contentDescription = "Logo de Feed Articles",
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Feed Articles",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    TutoComposeOct23Theme {
        SplashContent()
    }
}

