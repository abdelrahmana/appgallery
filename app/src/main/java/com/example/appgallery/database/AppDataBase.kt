package com.example.appgallery.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appgallery.datasource.dao.ImageDao
import com.example.appgallery.datasource.model.Image

@Database(entities = [Image::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}