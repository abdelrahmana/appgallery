package com.example.appgallery.datasource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image(
    @PrimaryKey val imagePath: String,
   // @ColumnInfo(name = "first_name") val firstName: String?,
    //@ColumnInfo(name = "last_name") val lastName: String?
)