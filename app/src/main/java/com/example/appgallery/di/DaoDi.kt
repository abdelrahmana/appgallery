package com.example.appgallery.di

import android.content.Context
import androidx.room.Room
import com.example.appgallery.database.AppDataBase
import com.example.appgallery.datasource.dao.ImageDao
import com.example.appgallery.datasource.model.Image
import com.example.appgallery.util.GetObjectGson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class, ActivityComponent::class,
    ServiceComponent::class)
class DaoDi {
    @Provides
    fun getImageDao(@ApplicationContext context: Context): AppDataBase {
       return Room.databaseBuilder(
            context,
            AppDataBase::class.java, "my_database"
        ).build()
    }
}