package com.example.appgallery.workmanger

import android.content.Context
import com.example.appgallery.datasource.model.Image
import com.example.appgallery.util.Util
import com.seven.util.PrefsModel
import com.seven.util.PrefsUtil
import dagger.hilt.android.qualifiers.ApplicationContext

interface WorkMangerInterface {
    fun getLatestFilteredArrayListToUpload(galleryImages :ArrayList<String>,localSaved : ArrayList<Image>):ArrayList<String>
    fun reRunTracking(util: Util) {}

}
class ImplementerNormalUpload(val context: Context) : WorkMangerInterface{
    override fun getLatestFilteredArrayListToUpload(
        galleryImages: ArrayList<String>,
        localSaved: ArrayList<Image>
    ): ArrayList<String> {
        // each time we use normal upload append the gallery saved shared prefs
        PrefsUtil().getSharedPrefs(context).edit()
            .putInt(PrefsModel.GALLERY_SAVED,galleryImages.size).apply()
        val filteredImages = getFilteredImages(
            localSaved ?: ArrayList(), galleryImages
        ) // get filtered arraylist now
       return filteredImages
    }

}

class ImplementerGalleryUpload(val filteredSize : Int):WorkMangerInterface {
    override fun reRunTracking(util: Util) {
        util.scheduleWork("image_tracker")

    }

    override fun getLatestFilteredArrayListToUpload(
        galleryImages: ArrayList<String>, // gallery image here is the difference between current and local
        localSaved: ArrayList<Image>
    ): ArrayList<String> {
        val newArrayList = ArrayList<String>()
        for (i in 0 until filteredSize){
            newArrayList.add(galleryImages[i])
        }
     //   val newRecentArray = galleryImages.take(filteredSize) // take the type only
        val filteredImages = getFilteredImages(
            localSaved ?: ArrayList(), newArrayList as ArrayList<String>
        ) // get filtered arraylist now

        return filteredImages
    }


}
fun getFilteredImages(imagesSaved: List<Image>, galleryImages: ArrayList<String>): ArrayList<String> {
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