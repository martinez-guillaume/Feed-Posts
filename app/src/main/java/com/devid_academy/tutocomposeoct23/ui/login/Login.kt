package com.devid_academy.tutocomposeoct23.ui.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.Screen
import com.devid_academy.tutocomposeoct23.theme.TutoComposeOct23Theme
import com.devid_academy.tutocomposeoct23.ui.register.RegisterContent
import com.devid_academy.tutocomposeoct23.ui.splash.SplashContent
import com.devid_academy.tutocomposeoct23.ui.splash.SplashViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel) {
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.successFlow.collect { _ ->
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.errorFlow.collect { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    LoginContent(
        goToRegister = {
            navController.navigate(Screen.Register.route)
        },
        onLoginClick = { login, password ->
            viewModel.loginUser(login, password)
        }
    )
}

@Composable
fun LoginContent(

    modifier: Modifier = Modifier,
    goToRegister: () -> Unit,
    onLoginClick: (String, String) -> Unit,

    ) {

    val (login, setLogin) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Connectez-vous",
            color = Color(0xFF0099CC),
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(50.dp))

        TextField(
            value = login,
            onValueChange = setLogin,
            label = { Text("Login") },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0099CC),
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color(0xFF0099CC),
                unfocusedLabelColor = Color(0xFF0099CC)
            )
        )

        Spacer(modifier = Modifier.height(50.dp))

        TextField(
            value = password,
            onValueChange = setPassword,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0099CC),
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color(0xFF0099CC),
                unfocusedLabelColor = Color(0xFF0099CC)
            )
        )

        Spacer(modifier = Modifier.height(130.dp))

        Button(
            onClick = { onLoginClick(login, password) },
            modifier = Modifier.fillMaxWidth(0.7f),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0099CC))
        ) {
            Text(
                text = "Se connecter",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Pas de compte ? Inscrivez-vous !",
            color = Color(0xFF0099CC),
            modifier = Modifier.clickable (onClick = goToRegister)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    TutoComposeOct23Theme {
        LoginContent(goToRegister = {}, onLoginClick = {_,_ ->})
    }
}