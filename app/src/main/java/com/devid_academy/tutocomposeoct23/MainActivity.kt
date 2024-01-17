package com.devid_academy.tutocomposeoct23

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devid_academy.tutocomposeoct23.theme.TutoComposeOct23Theme
import com.devid_academy.tutocomposeoct23.ui.crea.CreaScreen
import com.devid_academy.tutocomposeoct23.ui.crea.CreaViewModel
import com.devid_academy.tutocomposeoct23.ui.edit.EditScreen
import com.devid_academy.tutocomposeoct23.ui.edit.EditViewModel
import com.devid_academy.tutocomposeoct23.ui.login.LoginScreen
import com.devid_academy.tutocomposeoct23.ui.login.LoginViewModel
import com.devid_academy.tutocomposeoct23.ui.main.MainScreen
import com.devid_academy.tutocomposeoct23.ui.main.MainViewModel
import com.devid_academy.tutocomposeoct23.ui.register.RegisterScreen
import com.devid_academy.tutocomposeoct23.ui.register.RegisterViewModel
import com.devid_academy.tutocomposeoct23.ui.splash.SplashScreen
import com.devid_academy.tutocomposeoct23.ui.splash.SplashViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TutoComposeOct23Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {

    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val navController = rememberNavController()

    NavHost(navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController = navController, splashViewModel)
        }
        composable(Screen.Main.route) {
            val mainViewModel: MainViewModel = hiltViewModel()
            MainScreen(navController = navController, mainViewModel)
        }

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController = navController, loginViewModel)
        }

        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(navController = navController, registerViewModel)
        }
        composable(Screen.Crea.route) {
            val creaViewModel: CreaViewModel = hiltViewModel()
            CreaScreen(navController = navController, creaViewModel )
        }
        composable(
            route = "${Screen.Edit.route}/{articleDto}",
            arguments = listOf(navArgument("articleDto") { type = NavType.StringType })
        ) { backStackEntry ->
            val articleDataJson = backStackEntry.arguments?.getString("articleDto") ?: ""
            val editViewModel: EditViewModel = hiltViewModel()
            EditScreen(navController = navController, editViewModel, articleDataJson)
        }
    }
}







