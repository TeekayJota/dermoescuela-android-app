package com.example.dermoescuela_app.navigation

sealed class AppView(val route: String) {
    object Login : AppView("login")
    object Home : AppView("home")
}
