package com.example.appgallery.repo

import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.appgallery.apiconfig.UploadServiceLink
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.util.*
import javax.inject.Inject
class UploadRepo @Inject constructor(var serviceLinkUpload: UploadServiceLink?,@ApplicationContext val context: Context) {
    suspend fun uploadAmazonLink(/*query: LinkedHashMap<String, String>?*/
        query: String, restEndPoint: String, file: File
        ,completion:(String?, String?) -> Unit
    ) {
     /*   val requestBody: RequestBody = file.asRequestBody("binary/octet-stream".toMediaTypeOrNull())
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
             System.currentTimeMillis().toString() + ".jpg", fileBody)
      /*  val res =  serviceLinkUpload?.putUploadLink(/*"application/octet-stream"/*,restEndPoint*/,*/query!!,body)
         res?.onSuccess {
              completion("upload Success" ,null)
          }
          res?.onException {
              completion(null ,message.toString())
          }
          res?.onError {
              completion(null ,errorBody.toString())
          }*/
          */*
      */
        uploadImageToServer(convertFileToBytes(file),query,completion)
    }
    private fun uploadImageToServer(
        imageByte: ByteArray?,
        imageUrl: String,
        completion: (String?, String?) -> Unit
    ) {
        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.PUT, imageUrl,
                object : Response.Listener<String?> {
                    override fun onResponse(response: String?) {
                     //   Log.e("s3 response", response.toString())
                        completion("image uploaded successfylly",null)
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.e("error response", error.toString())
                        completion(null,"error response")

                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headerMap: MutableMap<String, String> = HashMap()
                 //   headerMap["header1"] = "header value"
                    headerMap["Content-Type"] = "binary/octet-stream"//"image/jpeg"
                    return headerMap
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    //Binary value data
                    return imageByte!!
                }
            }
        run {
            val socketTimeout = 30000
            val policy: RetryPolicy = DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            stringRequest.retryPolicy = policy
            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.cache.clear()
            requestQueue.add(stringRequest)
        }
    }

    //You can convert any file to byte array
    fun convertFileToBytes(file: File): ByteArray? {
        val size = file.length() as Long
        val bytes = ByteArray(size.toInt())
        try {
            val buf = BufferedInputStream(FileInputStream(file))
            buf.read(bytes, 0, bytes.size)
            buf.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return bytes
    }}