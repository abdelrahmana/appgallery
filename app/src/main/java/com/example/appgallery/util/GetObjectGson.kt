package com.example.appgallery.util

import android.content.Context
import com.example.appgallery.ui.home.model.PhotosArray
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GetObjectGson {

    fun getPhotoArray(objectData : String): ArrayList<PhotosArray>? { // this should return the object
        val jso = objectData
        val gson = Gson()
        val typeToken = object : TypeToken<ArrayList<PhotosArray>?>() {}.type
        val obj = gson.fromJson<ArrayList<PhotosArray>>(jso, typeToken) ?: null
        return obj

    }

}