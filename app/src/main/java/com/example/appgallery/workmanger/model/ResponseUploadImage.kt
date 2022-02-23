package com.example.appgallery.workmanger.model

data class ResponseUploadImage(
    val code: Int,
    val `data`: List<Data>,
    val message: String
)

data class Data(
    val hash: String,
    val id: Int,
    val imageExt: String,
    val imageName: String,
    val image_presignedUrl: String,
    val isDuplicate: Boolean,
    val resized_image_presignedUrl: String
)