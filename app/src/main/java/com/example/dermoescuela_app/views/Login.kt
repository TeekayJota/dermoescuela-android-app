package com.example.dermoescuela_app.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dermoescuela_app.R
import com.example.dermoescuela_app.auth.AuthManager
import com.example.dermoescuela_app.ui.theme.montserratFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x002FFFF5))
                .padding(paddingValues)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                LogoAndTitle()
                UsernameField(username) { username = it }
                PasswordField(password) { password = it }
                LoginButton(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    username = username,
                    password = password
                )
            }
        }
    }
}

@Composable
fun LogoAndTitle() {
    Image(
        painter = painterResource(id = R.drawable.logodermo),
        contentDescription = null,
        modifier = Modifier.padding(0.dp, 20.dp)
    )
    Text(
        text = "INICIA SESIÓN",
        fontFamily = montserratFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF223341),
        modifier = Modifier.padding(0.dp, 20.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = {
            Text(
                text = "Usuario",
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Medium
            )
        },
        modifier = Modifier
            .width(300.dp)
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.extraLarge,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(value: String, onValueChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = {
            Text(
                text = "Contraseña",
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Medium
            )
        },
        modifier = Modifier
            .width(300.dp)
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.extraLarge,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = image,
                    contentDescription = null,
                    modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun LoginButton(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    username: String,
    password: String
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current  // Obtener el contexto
    val authManager = remember { AuthManager(context) }  // Pasar el contexto a AuthManager

    OutlinedButton(
        onClick = {
            scope.launch {
                try {
                    // Llamar a la función de login del AuthManager con el username y password ingresados
                    val success = authManager.login(username, password)
                    if (success) {
                        // Navegar a la pantalla principal sin mostrar Snackbar
                        navController.navigate("home")
                    } else {
                        // Mostrar el Snackbar solo si los datos son incorrectos
                        snackbarHostState.showSnackbar("Datos Incorrectos")
                    }
                } catch (e: Exception) {
                    snackbarHostState.showSnackbar("Error al iniciar sesión")
                }
            }
        },
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFFA70532)
        ),
        modifier = Modifier.padding(vertical = 12.dp),
        border = BorderStroke(2.dp, Color(0xFFA70532))
    ) {
        Text(
            text = "INGRESAR",
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.Bold
        )
    }
}
