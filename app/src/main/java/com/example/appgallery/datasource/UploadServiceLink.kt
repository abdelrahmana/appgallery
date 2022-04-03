package com.example.appgallery.datasource

import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface UploadServiceLink {
   // @Headers("Accept: application/octet-stream")
   @Headers("Content-Type: binary/octet-stream")
   @Multipart
    @PUT("")
    suspend fun putUploadLink(/*@Header("Contentss-Type")contentType : String,*/
       @Url url : String,// @Body file: RequestBody
                              @Part file: MultipartBody.Part
       // @Part("file\"; filename=\"test.jpeg\" ")  file: RequestBody
                              ): ApiResponse<Any>
}