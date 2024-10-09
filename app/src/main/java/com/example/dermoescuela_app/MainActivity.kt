package com.example.dermoescuela_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dermoescuela_app.navigation.NavigationView
import com.example.dermoescuela_app.ui.theme.DermoescuelaappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DermoescuelaappTheme {
                NavigationView()
            }
        }
    }
}