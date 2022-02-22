package com.example.appgallery.di

import androidx.work.Constraints
import androidx.work.NetworkType
import com.example.appgallery.apiconfig.UploadServiceLink
import com.example.appgallery.apiconfig.WebService
import com.example.appgallery.repo.HomeRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class,
    ActivityComponent::class)
class RepoDi {
    @Provides
    fun getHomeRepo(webService: WebService,@RetrofitBuilder.AmazonRetrofit uploadServiceLink: UploadServiceLink): HomeRepo {
        return  HomeRepo(webService,uploadServiceLink)
    }
}