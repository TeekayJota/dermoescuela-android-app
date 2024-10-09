package com.example.dermoescuela_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dermoescuela_app.views.LoginView
import com.example.dermoescuela_app.views.HomeView

@Composable
fun NavigationView() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginView(navController = navController)
        }
        composable("home") {
            HomeView(navController = navController)
        }
    }
}
