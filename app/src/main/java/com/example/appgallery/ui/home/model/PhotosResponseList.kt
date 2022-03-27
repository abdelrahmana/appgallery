package com.example.appgallery.ui.home.model

data class PhotosResponseList(
    val code: Int,
    val `data`: List<Data>,
    val message: String
)

data class Data(
    val photosArray: List<PhotosArray>,
    val lastId : Int,
    val photosCount: Int,
    val taggedUser: String?,
    val taggedUserId: String?,
    val uploaderName: String?,
    val uploaderId: String?
)

data class PhotosArray(
    val id: String,
    val link: String,
    val resizedLink: String
)