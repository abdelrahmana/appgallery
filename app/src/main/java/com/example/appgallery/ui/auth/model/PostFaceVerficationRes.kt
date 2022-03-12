package com.example.appgallery.ui.auth.model

data class PostFaceVerficationRes(
    val `data`: List<Datas>
)

data class Datas(
    val prUrl: String
)