package com.example.dermoescuela_app.api

import com.example.dermoescuela_app.models.LoginRequest
import com.example.dermoescuela_app.models.TokenResponse
import com.example.dermoescuela_app.models.UserProfile
import com.example.dermoescuela_app.models.VideoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("auth/token/")
    suspend fun login(@Body loginRequest: LoginRequest): Response<TokenResponse>

    @GET("auth/profile/")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<UserProfile>

    @GET("videos/")
    suspend fun getAllVideos(@Header("Authorization") token: String): Response<List<VideoResponse>>
}