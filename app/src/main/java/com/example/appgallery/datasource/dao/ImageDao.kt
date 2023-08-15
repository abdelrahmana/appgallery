package com.example.appgallery.datasource.dao

import androidx.room.*
import com.example.appgallery.datasource.model.Image
import com.skydoves.sandwich.ApiResponse

@Dao
interface ImageDao {
    @Query("SELECT * FROM Image")
   suspend fun getAll(): List<Image>

    @Query("SELECT * FROM Image WHERE imagePath IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Image>

/*    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

 */
    @Insert
    fun insert(images: List<Image>)

    @Delete
    fun delete(image: Image)
}