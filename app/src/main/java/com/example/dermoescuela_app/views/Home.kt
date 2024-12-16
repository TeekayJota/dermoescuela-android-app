package com.example.dermoescuela_app.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dermoescuela_app.api.RetrofitClient
import com.example.dermoescuela_app.auth.AuthManager
import com.example.dermoescuela_app.models.VideoResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }

    var videoList by remember { mutableStateOf(listOf<VideoResponse>()) }
    var isLoading by remember { mutableStateOf(true) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    // Obtener perfil y lista de videos
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val profile = authManager.getUserProfile()
                if (profile != null) {
                    firstName = profile.firstName
                    lastName = profile.lastName
                }

                val token = authManager.getToken() ?: return@launch
                val response = RetrofitClient.createRetrofitWithToken(token)
                    .create(com.example.dermoescuela_app.api.ApiService::class.java)
                    .getAllVideos("Bearer $token")

                if (response.isSuccessful) {
                    response.body()?.let { videos ->
                        println("Videos recibidos: $videos") // Imprimir los videos
                        videoList = videos
                    }
                } else {
                    println("Error al cargar videos: ${response.message()}")
                }
            } catch (e: Exception) {
                println("Excepción en la carga de videos: ${e.message}")
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = { HomeTitle(firstName, lastName) }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            VideoList(videos = videoList, navController = navController, paddingValues = paddingValues)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTitle(firstName: String, lastName: String) {
    TopAppBar(
        title = {
            Text(
                text = "Bienvenido(a) $firstName $lastName",
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Composable
fun VideoList(videos: List<VideoResponse>, navController: NavController, paddingValues: PaddingValues) {
    if (videos.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay videos disponibles.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            items(videos) { video ->
                VideoItem(video = video, onClick = {
                    println("Clicked on video: ${video.sessionNumber}")
                })
            }
        }
    }
}


@Composable
fun VideoItem(video: VideoResponse, onClick: () -> Unit) {
    val thumbnailUrl = video.thumbnailUrl ?: "https://via.placeholder.com/150"
    val sessionNumber = video.sessionNumber.takeIf { it > 0 } ?: "N/A"
    val description = video.description.ifEmpty { "Sin descripción" }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        // Thumbnail como imagen
        Image(
            painter = rememberAsyncImagePainter(model = thumbnailUrl),
            contentDescription = "Thumbnail for $description",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .padding(end = 8.dp)
        )

        // Título del video
        Text(
            text = "Clase $sessionNumber: $description",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}