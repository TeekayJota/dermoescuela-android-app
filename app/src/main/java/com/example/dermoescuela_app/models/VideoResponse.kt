package com.example.dermoescuela_app.models

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("session_number") val sessionNumber: Int = 0,
    @SerializedName("description") val description: String = "Sin descripción",
    @SerializedName("module") val module: String? = null,
    @SerializedName("video_url") val videoUrl: String? = null,
    @SerializedName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerializedName("is_released") val isReleased: Boolean = false,
    @SerializedName("is_unlocked") val isUnlocked: Boolean = false,
    @SerializedName("pdf_url") val pdfUrl: String? = null
)