package com.example.dermoescuela_app.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.dermoescuela_app.api.ApiService
import com.example.dermoescuela_app.api.RetrofitClient
import com.example.dermoescuela_app.models.LoginRequest
import com.example.dermoescuela_app.models.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        // Crear una clave maestra para la encriptación
        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        // Crear EncryptedSharedPreferences para almacenar tokens de manera segura
        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secure_prefs",  // Nombre del archivo de preferencias
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // Iniciar sesión y guardar el token
    suspend fun login(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()?.access
                    saveToken(token!!)
                    return@withContext true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext false
        }
    }

    // Guardar el token de manera segura
    private fun saveToken(token: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    // Obtener el token almacenado
    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    // Eliminar el token almacenado al cerrar sesión
    suspend fun logout() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().remove("access_token").apply()
        }
    }

    // Obtener el perfil del usuario con el token
    suspend fun getUserProfile(): UserProfile? {
        return withContext(Dispatchers.IO) {
            try {
                val token = getToken()
                if (token != null) {
                    // Crear una instancia de Retrofit con el token dinámicamente
                    val apiServiceWithToken = RetrofitClient.createRetrofitWithToken(token).create(ApiService::class.java)
                    // Hacer la llamada para obtener el perfil
                    val response = apiServiceWithToken.getUserProfile("Bearer $token")
                    if (response.isSuccessful && response.body() != null) {
                        return@withContext response.body()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext null
        }
    }
}
