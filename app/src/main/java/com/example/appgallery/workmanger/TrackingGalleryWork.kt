package com.example.appgallery.workmanger

import android.content.Context
import android.os.Build

import android.provider.MediaStore
import android.provider.Settings
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.appgallery.notification.Notifications
import com.example.appgallery.util.Util
import com.seven.util.PrefsUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

@HiltWorker
class TrackingGalleryWork @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted  val params: WorkerParameters,
    val util: Util) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Do your actual work
    //   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           val workMangerDomain = WorkMangerDomain(applicationContext)
           //Notifications("image triggered ").runNotification(context)
           val galleryImages = util.loadImagesfromSDCard() // this descending
          val lastSavedInGallery =  PrefsUtil().getLocalGallerySavedSize(context) // size of previous gallery
           // before change
           if (galleryImages.size > lastSavedInGallery?:0) // in this case we are on adding new so upload this
               // lets do upload for that imag
           {     val filteredIndexes = galleryImages.size -lastSavedInGallery!!
              // for (i in 0 until filteredLoop) // loop time
              // {
            workMangerDomain.doWork(util,PrefsUtil(),ImplementerGalleryUpload(filteredIndexes))


             //  }
           }
    //   }

      /*  else
        params.triggeredContentUris.forEach {
           val file = File(it.path?:"")
            val notification =
                Notifications(file.absolutePath)

            notification.runNotification(context)
        }*/
        // Then start listening for more changes
       // util.scheduleWork("image_tracker")
        return Result.success()
    }


}