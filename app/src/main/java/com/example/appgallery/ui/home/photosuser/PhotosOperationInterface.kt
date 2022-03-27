package com.example.appgallery.ui.home.photosuser

import com.example.appgallery.apiconfig.WebService
import com.example.appgallery.ui.home.PhotosViewModel
import com.example.appgallery.ui.home.model.PhotosUserResponse
import com.skydoves.sandwich.ApiResponse

interface PhotosOperationInterface {
    suspend fun getCallApiResponse( webService: WebService, headerMap :HashMap<String,Any>): ApiResponse<PhotosUserResponse>
    fun setViewModelCall(hashMap: HashMap<String,Any>,model : PhotosViewModel)
    fun getUploaderKey(): String
}
class  FriendsUserImplementer() : PhotosOperationInterface {
    override suspend fun getCallApiResponse(
        webService: WebService,
        headerMap: HashMap<String, Any>
    ): ApiResponse<PhotosUserResponse> {
        return webService.getPhotosOfFriendsUser(headerMap)
    }

    override fun setViewModelCall(hashMap: HashMap<String, Any>, model: PhotosViewModel) {
        model.getPhotosUserOfFriend(hashMap)
    }

    override fun getUploaderKey(): String {
       return "taggeduserid"
    }
}
    class PhotosUserImplementer() : PhotosOperationInterface{
        override suspend fun getCallApiResponse( webService: WebService, headerMap :HashMap<String,Any>): ApiResponse<PhotosUserResponse> {
            return webService.getPhotosOfMeUser(headerMap)
        }

        override fun setViewModelCall(hashMap: HashMap<String, Any>,model: PhotosViewModel) {
            model.getPhotosOfMeUser(hashMap)

        }
        override fun getUploaderKey(): String {
            return "uploaderid"
        }
    }