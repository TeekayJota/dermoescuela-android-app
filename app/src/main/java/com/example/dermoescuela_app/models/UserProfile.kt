package com.example.dermoescuela_app.models

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("local_code") val localCode: String? = null,  // Campo opcional
    @SerializedName("local_description") val localDescription: String? = null,  // Campo opcional
    @SerializedName("profile_image_url") val profileImageUrl: String? = null  // Campo opcional
)