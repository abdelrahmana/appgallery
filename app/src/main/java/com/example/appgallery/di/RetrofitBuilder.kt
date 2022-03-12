package com.example.appgallery.di

import android.content.Context
import android.content.SharedPreferences
import androidx.hilt.work.HiltWorker
import com.example.appgallery.BuildConfig
import com.example.appgallery.apiconfig.UploadServiceLink
import com.example.appgallery.apiconfig.WebService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.seven.util.PrefsUtil
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

// this class to use custom retrofit buildr with differnt cases
 @Module
 @InstallIn(ViewModelComponent::class, FragmentComponent::class,ServiceComponent::class)
class RetrofitBuilder {
    var gson: Gson? = null
        get() {
            if (field == null) {
                field = GsonBuilder().setLenient().create()
            }
            return field
        }
        private set
    private var retrofit: Retrofit? = null
    private var okHttpAuthClient: OkHttpClient? = null
    private var okHttpLocalClient: OkHttpClient? = null


     @Provides
     @ViewModelScoped
     fun getRetrofitCall(@ApplicationContext context: Context?) : WebService {
         return getRetrofit(getHttpAuthClient(context!!),BASE_URL).create(
             WebService::class.java
         )
     }

    @Provides
    fun getDefultUrl():String {
        return ""
    }

    @Provides
    @AmazonRetrofit
    @ViewModelScoped
    fun getRetrofitCallAmazon(@ApplicationContext context: Context?,url :String) : UploadServiceLink {
        return getRetrofit(getHttpAuthClient(context!!),url).create(
            UploadServiceLink::class.java
        )
    }


    val loggingInterceptor: HttpLoggingInterceptor
        get() = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    @Provides
    fun getAuthInterceptor(context: Context?): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            chain.proceed(
                chain.request().newBuilder()
                    // .header("timezone", TimeZone.getDefault().id)
                    .header("Accept","application/json")
                 /*.header("lang-app", /*UtilKotlin.getLocalLanguage(context!!)?:"en"*/
                        /*PrefsUtil.getSharedPrefs(
                            context!!
                        )*/prefs.getString(PrefsModel.localLanguage, "en")?:"en")
                     .header("Authorization", PrefsUtil.getUserToken(context!!)?:"")*/
                    .header("accesstoken", /*"Bearer "+*/PrefsUtil().getUserToken(context!!)?:""
                            /*"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkZXRhaWxzIjp7ImlkIjoiMDUxODA1IiwiZGV2aWNlSWQiOiI2NjYtNjY2NjYtNTU1LTU1NTU1LTY2NiJ9LCJ0aW1lIjoiMjAvMDIvMjAyMiAxMTo0MDo0MCIsImlhdCI6MTY0NTM1NzI0MCwiZXhwIjoxNjUzMTMzMjQwfQ.45dtb-PwA7lj3IhVntMK5Q5jLD2luvBhMCKbdSFFcRw"*/
                    )
                     .method(original.method, original.body)
                     .build()
            )
        }
    }



    @Provides
    fun getHttpAuthClient(context: Context): OkHttpClient {
        if (okHttpAuthClient == null) {
            okHttpAuthClient = OkHttpClient.Builder()
                .followRedirects(false)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(getAuthInterceptor(context))
              //  .addInterceptor(getCustomErrorInterceptor(context))
                .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .build()
        }
        return okHttpAuthClient!!
    }

   /* @Provides
    fun getSharedPrefs(@ActivityContext context: Context?) : PrefsUtil {
        return PrefsUtil()
        //    val guest = PrefsUtil.getSharedPrefs(context).getBoolean(PrefsModel.isGuestUser,false)
    }*/

    fun getRetrofit(client: OkHttpClient?, BASE_URL: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(/*if (BuildConfig.DEBUG)*/ BASE_URL /*else LIVE_BASE_URL*/)
                .addConverterFactory(GsonConverterFactory.create(gson!!))
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
                .client(client!!)


                .build()
        }
        return retrofit!!
    }

    companion object {

      val  NETWORK_TIMEOUT :Long = 30

            var BASE_URL = "https://lw85zyto9a.execute-api.us-east-1.amazonaws.com/Prod/"//"https://lw85zyto9a.execute-api.us-east-1.amazonaws.com/"
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AmazonRetrofit

}