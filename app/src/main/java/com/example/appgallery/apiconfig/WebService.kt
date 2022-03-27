package com.example.appgallery.apiconfig

import com.example.appgallery.ui.auth.model.*
import com.example.appgallery.ui.home.model.PhotosResponseList
import com.example.appgallery.ui.home.model.PhotosUserResponse
import com.example.appgallery.workmanger.model.RequestUploadGson
import com.example.appgallery.workmanger.model.RequestUploadGsonObject
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
    suspend fun postGetApiLink(/*@Header("accesstoken") accessToken : String
                               = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkZXRhaWxzIjp7ImlkIjoiM
                               DUxODA1IiwiZGV2aWNlSWQiOiI2NjYtNjY2NjYtNTU1LTU1NTU1LTY2NiJ9LCJ0
                               aW1lIjoiMjAvMDIvMjAyMiAxMTo0MDo0MCIsImlhdCI6MTY0NTM1NzI0MCwiZXhwIjoxN
                               jUzMTMzMjQwfQ.45dtb-PwA7lj3IhVntMK5Q5jLD2luvBhMCKbdSFFcRw",*/
                               @Body gson : RequestUploadGson): ApiResponse<ResponseUploadImage>

    @POST("faceVerificationPresignedUrl")
    suspend fun postUploadPresignedFace(
        @Body gson : RequestUploadGsonObject
    ): ApiResponse<PresignedProfileRes>

    @POST("faceVerificationData")
    suspend fun postUploadFaceVerfication(
        @Body request : PostFaceVerficationRes
    ): ApiResponse<ResponseBody>

    @POST("otpLogin")
    suspend fun postLoginPhone(@Body requestLoginPhone: RequestLoginPhone): ApiResponse<ResponseLoginPhone>

    @POST("otpVerifier")
    suspend fun postVerfyingPhoneNumber(@Body requestLoginPhone: RequestOtpVerfying): ApiResponse<ResponseBody>

    @PUT("accountData")
    suspend fun postUpdatingInfo(@Body requestLoginPhone: PostUploadRequest): ApiResponse<ResponseBody>

    @GET("photosOfFriendsList")
    suspend fun getPhotosFriendsList(@HeaderMap hashMap: HashMap<String,Any>): ApiResponse<PhotosResponseList>

    @GET("photosOfMeList")
    suspend fun getPhotosOfMeList(@HeaderMap hashMap: HashMap<String,Any>): ApiResponse<PhotosResponseList>

    @GET("photosOfMeUser")
    suspend fun getPhotosOfMeUser(@HeaderMap hashMap: HashMap<String,Any>): ApiResponse<PhotosUserResponse>

    @GET("photosOfFriendsUser")
    suspend fun getPhotosOfFriendsUser(@HeaderMap hashMap: HashMap<String,Any>): ApiResponse<PhotosUserResponse>

}