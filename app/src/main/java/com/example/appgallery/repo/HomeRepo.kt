package com.example.appgallery.repo

import com.example.appgallery.apiconfig.UploadServiceLink
import com.example.appgallery.apiconfig.WebService
import com.example.appgallery.ui.auth.model.*
import com.example.appgallery.ui.home.model.PhotosArray
import com.example.appgallery.ui.home.model.PhotosResponseList
import com.example.appgallery.ui.home.photosuser.PhotosOperationInterface
import com.example.appgallery.workmanger.model.Datax
import com.example.appgallery.workmanger.model.RequestUploadGson
import com.example.appgallery.workmanger.model.RequestUploadGsonObject
import com.example.appgallery.workmanger.model.ResponseUploadImage
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import java.io.InputStream
import java.nio.file.Files
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class HomeRepo @Inject constructor(val webService: WebService,var serviceLinkUpload: UploadServiceLink?) {

    suspend fun uploadService(deviceId : String,completion: (String?, String?) -> Unit) {
        // lets get the home categories here and set the answer back to our viewmodel
        // should get all images here to upload later
       // val builder = MultipartBody.Builder()
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS", Locale("en"))

        // Create a calendar object that will convert the date and time value in milliseconds to date.

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
       val header =   "Android -- "+deviceId +" -- "+formatter.format(calendar.getTime())
        val res =  webService.postUploadSchedule(header)
        res.onSuccess {
            completion("success" ,null)

        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null ,"error happend")
        }
    }

    suspend fun uploadImage(jsonObject: RequestUploadGson,completion: (ResponseUploadImage?, String?) -> Unit) {

        val res =  webService.postGetApiLink(gson = jsonObject)
        res.onSuccess {
            completion(data ,null)

        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null ,"error happend")
        }
    }

    suspend fun uploadProfileImage(jsonObject: RequestUploadGsonObject,
                                   completion: (PresignedProfileRes?, String?) -> Unit) {

        val res =  webService.postUploadPresignedFace(gson = jsonObject)
        res.onSuccess {
            completion(data ,null)

        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null ,"error happend")
        }
    }
    suspend fun postFaceVerfication(jsonObject: PostFaceVerficationRes,
                                   completion: (ResponseBody?, String?) -> Unit) {

        val res =  webService.postUploadFaceVerfication(jsonObject)
        res.onSuccess {
            completion(data ,null)

        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null ,"There are no faces in the image")
        }
    }



    suspend fun loginPhoneNumber(requestLoginPhone: RequestLoginPhone,completion: (ResponseLoginPhone?, String?) -> Unit) {

        val res =  webService.postLoginPhone(requestLoginPhone)
        res.onSuccess {
            completion(data ,null)

        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null ,"error happend")
        }
    }

    suspend fun OtpVerfyingPost(requestLoginPhone: RequestOtpVerfying, completion: (ResponseBody?, String?) -> Unit) {

        val res =  webService.postVerfyingPhoneNumber(requestLoginPhone)
        res.onSuccess {
            completion(data ,null)

        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null ,"error happend")
        }
    }
    suspend fun postUpdateInfo(postUploadRequest: PostUploadRequest, completion: (ResponseBody?, String?) -> Unit) {

        val res =  webService.postUpdatingInfo(postUploadRequest)
        res.onSuccess {
            completion(data ,null)

        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null ,"error happend")
        }
    }
    suspend fun getPhotosFriendsList(headerMap : HashMap<String,Any>,
                                     completion: (List<com.example.appgallery.ui.home.model.Data>?, String?) -> Unit) {

        val res =  webService.getPhotosFriendsList(headerMap)
        res.onSuccess {
            completion(data?.data ,null)

        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null ,"error happend")
        }
    }
    suspend fun getPhotosUser(headerMap : HashMap<String,Any>, photos: PhotosOperationInterface,
                              completion: (List<PhotosArray>?, String?) -> Unit) {

        val res = photos.getCallApiResponse(webService,headerMap) //webService.getPhotosFriendsList(headerMap)
        res.onSuccess {
            completion(data?.data ,null)

        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null ,"error happend")
        }
    }
    suspend fun getPhotosOfMeList(headerMap : HashMap<String,Any>,
                                     completion: (List<com.example.appgallery.ui.home.model.Data>?, String?) -> Unit) {

        val res =  webService.getPhotosOfMeList(headerMap)
        res.onSuccess {
            completion(data?.data ,null)

        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null ,"error happend")
        }
    }
   /* suspend fun uploadAmazonLink(/*query: LinkedHashMap<String, String>?*/query: String,
                                                                          restEndPoint: String, file: File,completion:(String?, String?) -> Unit
    ) {
        val requestBody: RequestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
      //  val requestFile = RequestBody.create("binary/octet-stream".toMediaTypeOrNull(), Files.readAllBytes(file))
        val builder =   MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
      val fileNew =  RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
     /*   builder.addFormDataPart(
            "image", file.name,
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            /*RequestBody.create("multipart/form-data".toMediaTypeOrNull()*/
        )*/
       // val body = MultipartBody.Part.createFormData("image_file",file.name, requestFile)

        val fileBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("uploaded_file",
            "Img_" + "_" + System.currentTimeMillis() + ".jpg", fileBody)
        val res =  serviceLinkUpload?.putUploadLink(/*"application/octet-stream"/*,restEndPoint*/,*/query!!,body)
         res?.onSuccess {
              completion("upload Success" ,null)
          }
          res?.onException {
              completion(null ,message.toString())
          }
          res?.onError {
              completion(null ,"error happend")
          }
    }*/

    */
}