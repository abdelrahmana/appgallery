package com.example.appgallery.ui.auth.model

data class PresignedProfileRes(
    val code: Int,
    val `data`: List<String>,
    val message: String
)