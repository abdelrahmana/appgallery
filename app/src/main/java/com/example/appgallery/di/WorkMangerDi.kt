package com.example.appgallery.di

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import com.example.appgallery.util.Util
import com.seven.util.PrefsUtil
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
    ActivityComponent::class,ServiceComponent::class)
class WorkMangerDi {
    @Provides
    fun getConstrains(): Constraints {
        val myConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        return  myConstraints
    }
    @Provides
    fun getSharedPrefs(@ApplicationContext context: Context?) : PrefsUtil {
        return PrefsUtil()
        //    val guest = PrefsUtil.getSharedPrefs(context).getBoolean(PrefsModel.isGuestUser,false)
    }
    @Provides
    fun getUtil(@ApplicationContext context: Context): Util {
        return  Util(context)
    }
}