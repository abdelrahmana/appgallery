package com.example.appgallery.apiconfig

import com.google.android.gms.common.api.Api
import com.google.gson.JsonObject
import com.skydoves.sandwich.ApiResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UploadServiceLink {
   // @Headers("Accept: application/octet-stream")
   @Multipart
   @PUT
    suspend fun putUploadLink(@Header("Contentss-Type")contentType : String,
       @Url url : String,// @Body file: RequestBody
                              @Part("file\"; filename=\"test.jpeg\" ")  file: RequestBody
                              ): ApiResponse<Void>
}