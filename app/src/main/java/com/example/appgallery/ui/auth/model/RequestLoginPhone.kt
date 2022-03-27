package com.example.appgallery.ui.auth.model

import android.os.Build

data class RequestLoginPhone(
    val `data`: Data
)

data class Data(
    val countryCode: String,
    val countryCodeId: String,
    val deviceId: String,
    val tel: String,
    val deviceMake: String= Build.MANUFACTURER,
    val deviceModel: String=Build.MODEL
)