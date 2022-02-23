package com.example.appgallery.workmanger.model

data class RequestUploadGson(
    val `data`: List<Datax>
)

data class Datax(
    var hash: String,
    var id: Int,
    var imageExt: String,
    var imageName: String
)