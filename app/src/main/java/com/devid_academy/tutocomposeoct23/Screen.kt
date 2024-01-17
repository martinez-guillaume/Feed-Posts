package com.devid_academy.tutocomposeoct23

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object Edit : Screen("edit")
    object Login : Screen("login")
    object Register : Screen("register")
    object Crea : Screen("crea")
}