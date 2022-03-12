package com.example.appgallery.di

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import com.example.appgallery.apiconfig.UploadServiceLink
import com.example.appgallery.apiconfig.WebService
import com.example.appgallery.repo.HomeRepo
import com.example.appgallery.repo.UploadRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class,
    ActivityComponent::class)
class RepoDi {
    @Provides
    fun getHomeRepo(webService: WebService,
                    @RetrofitBuilder.AmazonRetrofit uploadServiceLink: UploadServiceLink): HomeRepo {
        return  HomeRepo(webService,uploadServiceLink)
    }

    @Provides
    fun uploadRepo(
        @RetrofitBuilder.AmazonRetrofit uploadServiceLink: UploadServiceLink,
        @ApplicationContext context: Context): UploadRepo {
        return  UploadRepo(uploadServiceLink,context)
    }
}