package com.example.appgallery.workmanger.model

data class RequestUploadGson(
    val `data`: List<Datax>
)

data class RequestUploadGsonObject(
    val `data`: Dataxx
)
data class Dataxx(
    var imageExt: String)
data class Datax(
    var hash: String,
    var id: Int,
    var imageExt: String,
    var imageName: String
)