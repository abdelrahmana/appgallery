package com.example.appgallery.ui.auth.model

data class RequestOtpVerfying(
    val `data`: OtpVerfyingData
)

data class OtpVerfyingData(
    val countryCode: String,
    val tel: String,
    val verificationCode: String)