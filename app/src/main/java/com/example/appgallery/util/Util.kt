package com.example.appgallery.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.work.*
import com.example.appgallery.workmanger.TrackingGalleryWork
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Util @Inject constructor(@ApplicationContext val context: Context) {
    @SuppressLint("HardwareIds")
    fun getDeviceId() : String {
   return Settings.Secure.getString(context.getContentResolver(),
            Settings.Secure.ANDROID_ID)
    }

    fun scheduleWork(tag: String) {
        val photoCheckBuilder = OneTimeWorkRequest.Builder(TrackingGalleryWork::class.java)
    /*    val photoCheckBuilder = PeriodicWorkRequest.Builder(TrackingGalleryWork::class.java,
            15, TimeUnit.SECONDS/*,5, TimeUnit.MINUTES*/)*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            photoCheckBuilder.setConstraints(
                Constraints.Builder()
                    .addContentUriTrigger(MediaStore.Images.Media.INTERNAL_CONTENT_URI, true)
                    .addContentUriTrigger(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true)
                    .build()
            )
        }
        val photoCheckWork = photoCheckBuilder.build()
       // val instance = WorkManager.getInstance(context)
        WorkManager.getInstance(context).enqueueUniqueWork(tag,
            ExistingWorkPolicy.REPLACE, photoCheckWork)

    }

    fun getFacesImage(
        arrayList: ArrayList<String>,
        callBackImages: (HashMap<String, ArrayList<String>>) -> Unit
    ) {
        val nonFaceImages = ArrayList<String>()
        val faceImages = ArrayList<String>()

        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(
                FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(
                FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
           // .enableTracking()
            .build()
        // loop arraylist
        for (index in 0 until arrayList.size) {
            try {
                val image = InputImage.fromFilePath(context, File(arrayList.get(index)).toUri())
                val detector = FaceDetection.getClient(options)
                val result = detector.process(image) // detector listener
                    .addOnSuccessListener { faces ->
                        // Task completed successfully
                        // ...
                        faceImages.add(arrayList.get(index))
                        checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                        nonFaceImages.add(arrayList.get(index))

                        checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

                    }


            } catch (e: Exception) {
                nonFaceImages.add(arrayList.get(index))
                checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

            }

        }
      /*  arrayList.forEachIndexed { index, url ->
            try {
              val image = InputImage.fromFilePath(context, File(url).toUri())
                val detector = FaceDetection.getClient(options)
                val result = detector.process(image) // detector listener
                    .addOnSuccessListener { faces ->
                        // Task completed successfully
                        // ...
                        faceImages.add(url)
                        checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                        nonFaceImages.add(url)

                        checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

                    }


            } catch (e: Exception) {
                nonFaceImages.add(url)
                checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

            }

        }*/

    }
    private fun checkLastElement(index : Int, lastIndex : Int, callBackImages:
        (HashMap<String, ArrayList<String>>) -> Unit, faceImages : ArrayList<String>,
    nonFaceImages: ArrayList<String>) {
        if (index == lastIndex) // last item now call the call back
        {
            val hashMap =HashMap<String, ArrayList<String>>().also {
                it.put(Util.FACES, faceImages)
                it.put(Util.NORMAL_IMAGES, nonFaceImages)

            }
            callBackImages.invoke(hashMap)
        }
    }
    fun loadImagesfromSDCard(): ArrayList<String> {
        val uri: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor?
        val column_index_data: Int
        val column_index_folder_name: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null

        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
       val orderBy = MediaStore.Images.Media.DATE_TAKEN
        cursor = context.contentResolver.query(uri, projection, null, null, orderBy + " DESC")

        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }

    fun checkPermssionGrantedForImageAndFile(
        context: Activity,
        requestCode: Int,
        fragment: Fragment?,
        requestPermssions: ActivityResultLauncher<Array<String>>
    ) : Boolean {
        var allow = false
        // imageView.setOnClickListener {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermssions.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }else{
                allow = true

            }
        } else {
            // startCameraNow()
            allow =  true

        }

        //  }
        return allow
    }
    companion object {
        val COroutineWorker: String ="myWorkManager"
        val FACES = "FACES"
        val NORMAL_IMAGES = "NORMAL"
        val PERMSSIONS_FILES = 100
    }
}