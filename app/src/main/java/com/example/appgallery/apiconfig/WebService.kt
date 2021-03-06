package com.example.appgallery.apiconfig

import com.example.appgallery.workmanger.model.RequestUploadGson
import com.example.appgallery.workmanger.model.ResponseUploadImage
import com.google.gson.JsonObject
import com.skydoves.sandwich.ApiResponse
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface WebService {
    @POST("processingTask")
   suspend fun postUploadSchedule(@Header("deviceid")device: String/*@Body requestBody: RequestBody?*/): ApiResponse<ResponseBody>

    @POST("uploadPresignedUrl")
    suspend fun postGetApiLink(@Header("accesstoken") accessToken : String
                               = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkZXRhaWxzIjp7ImlkIjoiMDUxODA1IiwiZGV2aWNlSWQiOiI2NjYtNjY2NjYtNTU1LTU1NTU1LTY2NiJ9LCJ0aW1lIjoiMjAvMDIvMjAyMiAxMTo0MDo0MCIsImlhdCI6MTY0NTM1NzI0MCwiZXhwIjoxNjUzMTMzMjQwfQ.45dtb-PwA7lj3IhVntMK5Q5jLD2luvBhMCKbdSFFcRw",
                               @Body gson : RequestUploadGson): ApiResponse<ResponseUploadImage>


}