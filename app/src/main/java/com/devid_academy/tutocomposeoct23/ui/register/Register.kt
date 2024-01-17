package com.devid_academy.tutocomposeoct23.ui.register

import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.tutocomposeoct23.theme.TutoComposeOct23Theme
import com.devid_academy.tutocomposeoct23.ui.splash.SplashContent
import com.devid_academy.tutocomposeoct23.ui.splash.SplashViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.devid_academy.tutocomposeoct23.Screen

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.successFlow.collect { _ ->
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    RegisterContent(
        onRegisterClick = { login, password, confirmPassword ->
            viewModel.registerUser(login, password, confirmPassword)
        }
    )
}


@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    onRegisterClick: (String, String,String) -> Unit
){

    val (login, setLogin) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val (confirmPassword, setConfirmPassword) = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Nouveau Compte",
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

        Spacer(modifier = Modifier.height(50.dp))

        TextField(
            value = confirmPassword,
            onValueChange = setConfirmPassword,
            label = { Text("Confirmation Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0099CC),
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color(0xFF0099CC),
                unfocusedLabelColor = Color(0xFF0099CC)
            )
        )

        Spacer(modifier = Modifier.height(100.dp))

        Button(
            onClick = { onRegisterClick(login, password, confirmPassword) },
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0099CC))
        ) {
            Text(
                text = "S'inscrire",
                color = Color.White
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    TutoComposeOct23Theme {
        RegisterContent(onRegisterClick = { _, _, _ -> })
    }
}