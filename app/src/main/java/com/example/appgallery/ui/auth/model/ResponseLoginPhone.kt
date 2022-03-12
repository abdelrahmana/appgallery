package com.example.appgallery.ui.auth.model

data class ResponseLoginPhone(
    val code: Int,
    val `data`: Datax,
    val message: String
)

data class Datax(
    val otpType: String,
    var registrationStepId: Int, // 1 not register , 2 need to upload photo, 3 uploaded successfully
    val token: String
)