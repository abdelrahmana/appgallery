package com.example.appgallery.ui.auth.model

data class RequestLoginPhone(
    val `data`: Data
)

data class Data(
    val countryCode: String,
    val countryCodeId: String,
    val deviceId: String,
    val tel: String,
    val deviceMake: String="default_make",
    val deviceModel: String="model",
)