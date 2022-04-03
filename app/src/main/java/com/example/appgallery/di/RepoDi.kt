package com.example.appgallery.di

import android.content.Context
import com.example.appgallery.database.AppDataBase
import com.example.appgallery.datasource.UploadServiceLink
import com.example.appgallery.datasource.WebService
import com.example.appgallery.repo.HomeRepo
import com.example.appgallery.repo.UploadRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class,
    ActivityComponent::class)
class RepoDi {
    @Provides
    fun getHomeRepo(webService: WebService,
                    @RetrofitBuilder.AmazonRetrofit uploadServiceLink: UploadServiceLink,
        localDataBase: AppDataBase): HomeRepo {
        return  HomeRepo(webService,uploadServiceLink,localDataBase)
    }

    @Provides
    fun uploadRepo(
        @RetrofitBuilder.AmazonRetrofit uploadServiceLink: UploadServiceLink,
        @ApplicationContext context: Context): UploadRepo {
        return  UploadRepo(uploadServiceLink,context)
    }


}