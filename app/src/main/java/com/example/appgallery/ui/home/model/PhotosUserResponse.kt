package com.example.appgallery.ui.home.model

data class PhotosUserResponse(
    val code: Int,
    val `data`: List<PhotosArray>,
    val message: String
)

/*data class Data(
    val id: String,
    val photoLink: String,
    val resizedPhotoLink: String
)*/