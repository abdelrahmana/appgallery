package com.example.appgallery.ui.auth.model

data class PostUploadRequest(
    val `data`: DataProfile
)

data class DataProfile(
    val genderId: String="1",
    val name: String,
    val oldData: OldData=OldData(),
    val username: String ="N/A"
)

data class OldData(
    val genderId: String="",
    val name: String="",
    val username: String=""
)