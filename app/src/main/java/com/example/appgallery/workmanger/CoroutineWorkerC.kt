package com.example.appgallery.workmanger

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.appgallery.di.RetrofitBuilder
import com.example.appgallery.notification.Notifications
import com.example.appgallery.repo.HomeRepo
import com.example.appgallery.repo.UploadRepo
import com.example.appgallery.util.Util
import com.example.appgallery.workmanger.model.Datax
import com.example.appgallery.workmanger.model.RequestUploadGson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import java.net.URL
import java.net.URLDecoder
import java.io.*


@HiltWorker
public open class CoroutineWorkerC  @AssistedInject constructor(
    @Assisted  val context: Context,
    @Assisted params: WorkerParameters,//,
    val util : Util
    // val homeRepo: HomeRepo
    ) : CoroutineWorker(context, params) {


    override suspend fun doWork(): Result {
       val res =  CoroutineScope(Dispatchers.IO).async {
           // this should access repo directly
           //  Toast.makeText(applicationContext,"success",Toast.LENGTH_SHORT).show()

             if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                       context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){

                 val homeRepo =  HomeRepo(RetrofitBuilder().getRetrofitCall(applicationContext),
                     RetrofitBuilder().getRetrofitCallAmazon(applicationContext,
                         "https://lw85zyto9a.execute-api.us-east-1.amazonaws.com/Prod/"))
              /* HomeRepo(RetrofitBuilder().getRetrofitCall(applicationContext))*/
                 /*homeRepo.uploadService(util.getDeviceId()) { success, errors ->
                   success?.let { it ->
                       val notification = Notifications("passed")
                       //     notification.runNotification(applicationContext)
                   }
                   errors?.let { it ->
                       val notification = Notifications(it)
                       notification.runNotification(applicationContext)
                   }
               }*/
                 val requestUploadGson = RequestUploadGson(ArrayList<Datax>().also { it.add(Datax("dasd",123,"jpg",
                 "image")) })
               //  val json = JsonObject()
             //    json.addProperty("data",Gson().toJson(requestUploadGson.data))
              //   val result = Gson().toJson(requestUploadGson)
                 homeRepo.uploadImage(requestUploadGson) { success, errors ->
                     success?.let { it ->
                         val baseUrl: String = it.data[0].image_presignedUrl.split("original/").get(0)+"original/"
                         val queryUrl: String = it.data[0].image_presignedUrl.split("original/").get(1)
                         val restEndPoint = queryUrl.split("?").get(0).toString()
                         val queries = queryUrl.split("?").get(1).toString()

                         //baseUrl = baseUrl
                         val newRepo =  UploadRepo(RetrofitBuilder().getRetrofitCallAmazon(applicationContext,
                                 baseUrl),applicationContext)
                       /*  homeRepo.serviceLinkUpload = RetrofitBuilder().getRetrofitCallAmazon(applicationContext,
                             baseUrl)*/
                         val notification = Notifications("link "+it.data[0].image_presignedUrl)
                              notification.runNotification(applicationContext)

                         CoroutineScope(Dispatchers.IO).launch {
                        putUploadLink(newRepo,it.data[0].image_presignedUrl,restEndPoint).await()
                         }
                     }
                     errors?.let { it ->
                         val notification = Notifications(it)
                         notification.runNotification(applicationContext)
                     }
                 }

           }
           Result.success()
       }
       return res.await()

    }

     suspend fun putUploadLink(homeRepo: UploadRepo, queryUrl: String, restEndPoint: String): Deferred<Unit> {
        val res =  CoroutineScope(Dispatchers.IO).async {
         //   var newQuery = "{"+ queryUrl+"}"
         //   val gson = Gson().toJson(newQuery)
          /* val keyValuePairs: List<String> =
                queryUrl.split("&") //split the string to creat key-value pairs

            val map: LinkedHashMap<String, String> = LinkedHashMap()

            for (pair in keyValuePairs)  //iterate over the pairs
            {
                val entry = pair.split("=").toTypedArray() //split the pairs to get key and value
                map[entry[0]/*.trim { it <= ' ' }*/] =
                    entry[1]/*.trim { it <= ' ' }*////add them to the hashmap and trim whitespaces
            }*/
           /* val attributes: HashMap<String, Any>? = Gson().fromJson(
                gson,
                HashMap::class.java
            ) as HashMap<String, Any>?*/
          /*  val am: AssetManager = context.getAssets()
            val inputStream: InputStream = am.open("Capture.PNG")
            inputStream*/
            val allImages = util.loadImagesfromSDCard()
            allImages.let { it ->
                val file=File(it[0])

                homeRepo.uploadAmazonLink(/*splitQuery(queryUrl)*/queryUrl,
                    restEndPoint,file
                ) { success, errors ->
                    success?.let { it ->
                        val notification = Notifications("success amazon uploaded")
                        notification.runNotification(applicationContext)
                    }
                    errors?.let { it ->
                        val notification = Notifications(it)
                        notification.runNotification(applicationContext)
                    }
                }
            }
       /*     val url = URL(queryUrl)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
          //  urlConnection.setRequestProperty("Content-Type", "binary/octet-stream")
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("PUT")
            urlConnection.setRequestProperty("Accept", "*//*");
            urlConnection.setRequestProperty("Content-type", "binary/octet-stream")
            try {
                val conn = url.openConnection() as HttpURLConnection
                if (conn.responseCode == HttpsURLConnection.HTTP_OK) {
                    // Do normal input or output stream reading
                    val notification = Notifications("success amazon uploaded")
                    notification.runNotification(applicationContext)
                } else {
                    val br = BufferedReader(InputStreamReader(conn.getErrorStream()))
                    val builder = StringBuilder()

                    try {
                        var line: String?
                        while (br.readLine().also { line = it } != null) {
                            builder.append(line)
                        }
                    } finally {
                        br.close()
                    }
             //       val notification = Notifications(builder.toString())
                //    notification.runNotification(applicationContext)
                }
            }  catch (e: IOException) {
                val notification = Notifications("error")
                notification.runNotification(applicationContext)
            }*/
        }
        return res
    }
    @Throws(UnsupportedEncodingException::class)
    open fun splitQuery(url: String): LinkedHashMap<String, String>? {
        val query_pairs: LinkedHashMap<String, String> = LinkedHashMap()
        val query: String = URL(url).getQuery()
        val pairs = query.split("&").toTypedArray()
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            query_pairs[URLDecoder.decode(pair.substring(0, idx))] =
                URLDecoder.decode(pair.substring(idx + 1))
        }
        return query_pairs
    }
}