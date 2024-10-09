package com.example.dermoescuela_app.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dermoescuela_app.auth.AuthManager
import com.example.dermoescuela_app.ui.theme.montserratFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scope.launch {
            val profile = authManager.getUserProfile()
            if (profile != null) {
                firstName = profile.firstName
                lastName = profile.lastName
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                HomeTitle(firstName, lastName)
                Spacer(modifier = Modifier.height(24.dp))
                LogoutButton(navController = navController)
            }
        }
    }
}

@Composable
fun HomeTitle(firstName: String, lastName: String) {
    Text(
        text = "Bienvenido $firstName $lastName",
        fontFamily = montserratFontFamily,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF223341),
    )
}

@Composable
fun LogoutButton(
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current  // Obtener el contexto
    val authManager = remember { AuthManager(context) }  // Pasar el contexto a AuthManager

    OutlinedButton(
        onClick = {
            scope.launch {
                try {
                    // Llamar a la función de logout del AuthManager
                    authManager.logout()

                    // Navegar de regreso a la pantalla de login
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }  // Limpiar el historial de navegación
                    }
                    // Ya no se muestra el Snackbar
                } catch (e: Exception) {
                    // Solo en caso de error mostramos el Snackbar
                    navController.navigate("login") // En caso de error, vuelve a la pantalla de inicio de sesión de todos modos
                }
            }
        },
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFFA70532)
        ),
        modifier = Modifier.padding(vertical = 12.dp),
        border = BorderStroke(1.dp, Color(0xFFA70532))
    ) {
        Text(
            text = "CERRAR SESIÓN",
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.Bold
        )
    }
}


