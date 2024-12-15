package com.example.dermoescuela_app.models

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("session_number") val sessionNumber: Int,
    @SerializedName("description") val description: String,
    @SerializedName("module") val module: String?,
    @SerializedName("video_url") val videoUrl: String?,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?,
    @SerializedName("is_released") val isReleased: Boolean,
    @SerializedName("is_unlocked") val isUnlocked: Boolean,
    @SerializedName("pdf_url") val pdfUrl: String?
)