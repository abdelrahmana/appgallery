package com.example.appgallery.workmanger

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.work.ListenableWorker
import com.example.appgallery.datasource.model.Image
import com.example.appgallery.di.DaoDi
import com.example.appgallery.di.RetrofitBuilder
import com.example.appgallery.notification.Notifications
import com.example.appgallery.repo.HomeRepo
import com.example.appgallery.repo.UploadRepo
import com.example.appgallery.util.Util
import com.example.appgallery.workmanger.model.Datax
import com.example.appgallery.workmanger.model.RequestUploadGson
import com.seven.util.PrefsModel
import com.seven.util.PrefsUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

class WorkMangerDomain @Inject constructor(@ApplicationContext val applicationContext: Context) {

   /* fun properApiLinkToCall(homeRepo : HomeRepo,lastUnUploadedImage : String,util : Util){
        val file = File(lastUnUploadedImage)
        val hashed =  util.md5(util.getBase64Image(lastUnUploadedImage)?:"")?:""
        val requestUploadGson = RequestUploadGson(ArrayList<Datax>().also {
            it.add(
                Datax(
                    hashed, System.currentTimeMillis().toInt(), /*"jpg"*/ file.extension,
                    /*"image"*/file.name
                )
            )
        })
        //  val json = JsonObject()
        //    json.addProperty("data",Gson().toJson(requestUploadGson.data))
        //   val result = Gson().toJson(requestUploadGson)
        // get amazon url
        CoroutineScope(Dispatchers.IO).launch {
            homeRepo.uploadImage(requestUploadGson) { success, errors ->
                success?.let { it ->
                    val baseUrl: String =
                        it.data[0].image_presignedUrl.split("original/")
                            .get(0) + "original/"
                    val queryUrl: String =
                        it.data[0].image_presignedUrl.split("original/").get(1)
                    val restEndPoint = queryUrl.split("?").get(0).toString()
                    val queries = queryUrl.split("?").get(1).toString()

                    //baseUrl = baseUrl
                    val newRepo = UploadRepo(
                        RetrofitBuilder().getRetrofitCallAmazon(
                            applicationContext,
                            baseUrl
                        ), applicationContext
                    )
                    /*  homeRepo.serviceLinkUpload = RetrofitBuilder().getRetrofitCallAmazon(applicationContext,
            baseUrl)*/
                    val notification =
                        Notifications("link " + it.data[0].image_presignedUrl)
                    notification.runNotification(applicationContext)
                    //   CoroutineScope(Dispatchers.IO).async {
                    //    homeRepo.getDataBaseLocalImages { localImagesUploaded, errorsLocalDb ->
                    // current uploaded image
                    CoroutineScope(Dispatchers.IO).launch {
                        //         results.await()
                        putUploadLinkAmazon(lastUnUploadedImage,
                            // localImagesUploaded,
                            homeRepo,
                            newRepo,
                            it.data[0].image_presignedUrl,
                            restEndPoint
                        ).await()
                    }
                    /* errorsLocalDb?.let { it ->
                    val notifications = Notifications(it)
                    notifications.runNotification(applicationContext)
                }*/
                    //        }
                }

                //  }
                errors?.let { it ->
                    val notification = Notifications(it)
                    notification.runNotification(applicationContext)
                }
            }
        }
    }*/
   suspend fun doWork(util: Util,prefsUtil:PrefsUtil,workManagerInterface: WorkMangerInterface)
   : ListenableWorker.Result {
       val res =  CoroutineScope(Dispatchers.IO).async {
           // this should access repo directly
           //  Toast.makeText(applicationContext,"success",Toast.LENGTH_SHORT).show()

           if (applicationContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
               applicationContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){

               // prepare repo
               val homeRepo =  HomeRepo(RetrofitBuilder().getRetrofitCall(applicationContext),
                   RetrofitBuilder().getRetrofitCallAmazon(applicationContext,
                       "https://lw85zyto9a.execute-api.us-east-1.amazonaws.com/Prod/"),
                   DaoDi().getImageDao(applicationContext))
               // get local database
               CoroutineScope(Dispatchers.IO).launch {
                   homeRepo.getDataBaseLocalImages { localImagesUploaded, errorsLocalDb ->
                       val galleryImages = util.loadImagesfromSDCard()
                   val filteredImages = workManagerInterface.getLatestFilteredArrayListToUpload(galleryImages,
                           localImagesUploaded as ArrayList<Image>
                       )
                    /*   val filteredImages = getFilteredImages(
                           localImagesUploaded ?: ArrayList(), galleryImages
                       ) // get filtered arraylist now
*/
                       if (filteredImages.isNotEmpty()) {
                           val lastUnUploadedImage = filteredImages[0] // descending
                           val file = File(lastUnUploadedImage)
                           val hashed =  util.md5(util.getBase64Image(lastUnUploadedImage)?:"")?:""
                           val requestUploadGson = RequestUploadGson(ArrayList<Datax>().also {
                               it.add(
                                   Datax(
                                       hashed, System.currentTimeMillis().toInt(), /*"jpg"*/ file.extension,
                                       /*"image"*/file.name
                                   )
                               )
                           })
                           CoroutineScope(Dispatchers.IO).launch {
                               homeRepo.uploadImage(requestUploadGson) { success, errors ->
                                   success?.let { it ->
                                       val baseUrl: String =
                                           it.data[0].image_presignedUrl.split("original/")
                                               .get(0) + "original/"
                                       val queryUrl: String =
                                           it.data[0].image_presignedUrl.split("original/").get(1)
                                       val restEndPoint = queryUrl.split("?").get(0).toString()
                                       val queries = queryUrl.split("?").get(1).toString()

                                       //baseUrl = baseUrl
                                       val newRepo = UploadRepo(
                                           RetrofitBuilder().getRetrofitCallAmazon(
                                               applicationContext,
                                               baseUrl
                                           ), applicationContext
                                       )
                                       /*  homeRepo.serviceLinkUpload = RetrofitBuilder().getRetrofitCallAmazon(applicationContext,
                               baseUrl)*/
                                       val notification =
                                           Notifications("link " + it.data[0].image_presignedUrl)
                                       notification.runNotification(applicationContext)
                                       //   CoroutineScope(Dispatchers.IO).async {
                                       //    homeRepo.getDataBaseLocalImages { localImagesUploaded, errorsLocalDb ->
                                       // current uploaded image
                                       CoroutineScope(Dispatchers.IO).launch {
                                           //         results.await()
                                           putUploadLinkAmazon(lastUnUploadedImage,
                                               // localImagesUploaded,
                                               homeRepo,
                                               newRepo,
                                               it.data[0].image_presignedUrl,
                                               restEndPoint,
                                               util,prefsUtil,workManagerInterface
                                           ).await()
                                       }
                                       /* errorsLocalDb?.let { it ->
                                       val notifications = Notifications(it)
                                       notifications.runNotification(applicationContext)
                                   }*/
                                       //        }
                                   }

                                   //  }
                                   errors?.let { it ->
                                       val notification = Notifications(it)
                                       notification.runNotification(applicationContext)
                                   }
                               }
                           }
                       } else
                           workManagerInterface.reRunTracking(util)
                   }
               }


           }
           ListenableWorker.Result.success()
       }
       return res.await()

   }

    suspend fun putUploadLinkAmazon(
        lastUnUploadingImage: String,
        /*imagesSaved : List<Image>?,*/
        homeRepo: HomeRepo,
        uploadRepo: UploadRepo,
        queryUrl: String,
        restEndPoint: String,
        util: Util,
        prefsUtil: PrefsUtil,
        workManagerInterface: WorkMangerInterface
    ): Deferred<Unit> {
        val res =  CoroutineScope(Dispatchers.IO).async {
            lastUnUploadingImage.let { currentImage ->
                val file=File(currentImage/*[0]*/)

                uploadRepo.uploadAmazonLink(/*splitQuery(queryUrl)*/queryUrl,
                    restEndPoint,file
                ) { success, errors ->
                    success?.let { it ->
                        val notification = Notifications("success amazon uploaded")
                        notification.runNotification(applicationContext)
                        CoroutineScope(Dispatchers.IO).launch {
                            homeRepo.insertToDataBase(ArrayList<Image>().also {it.add(Image(currentImage)) })
                            doWork(util,prefsUtil,workManagerInterface)
                        }
                    }
                    errors?.let { it ->
                        val notification = Notifications(it)
                        notification.runNotification(applicationContext)
                    }
                }
            }

        }
        return res
    }

  private fun getFilteredImages(imagesSaved: List<Image>, galleryImages: ArrayList<String>): ArrayList<String> {
        val newArrayList = ArrayList<String>()
        newArrayList.addAll(galleryImages)
        for (i in 0 until imagesSaved.size) { // local data base images
            for (j in 0 until galleryImages.size){ // gallery images
                if (imagesSaved[i].imagePath == galleryImages[j]) {
                    newArrayList.remove(galleryImages[j]) // remove item in filteration
                    break // break first loop
                }
            }
        }
        return newArrayList
    }
}