package com.example.appgallery.ui.auth

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.appgallery.base.BaseViewModel
import com.example.appgallery.di.RetrofitBuilder
import com.example.appgallery.notification.Notifications
import com.example.appgallery.repo.HomeRepo
import com.example.appgallery.repo.UploadRepo
import com.example.appgallery.ui.auth.model.*
import com.example.appgallery.util.DissMissProgress
import com.example.appgallery.util.ShowProgress
import com.example.appgallery.workmanger.model.Datax
import com.example.appgallery.workmanger.model.RequestUploadGson
import com.example.appgallery.workmanger.model.RequestUploadGsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

// this viewpager is used within fragment
// specifically when running new fragment and notifying fragment with this change
@HiltViewModel
class SignPhoneViewModel @Inject constructor(private val myRepo: HomeRepo,
                                             private val uploadRepo: UploadRepo): BaseViewModel() {
   private val _responseDataCode = MutableLiveData<ResponseLoginPhone?>() // lets make this generic to use it with all apis
     val responseDataLive :LiveData<ResponseLoginPhone?> =_responseDataCode // lets make this generic to use it with all apis

    private val _confirmCodeRes = MutableLiveData<ResponseBody?>() // lets make this generic to use it with all apis
    val confirmCodeLive :LiveData<ResponseBody?> =_confirmCodeRes // lets make this generic to use it with all apis

    private val _uploadUserInfo = MutableLiveData<ResponseBody?>() // lets make this generic to use it with all apis
    val uploadUserInfo :LiveData<ResponseBody?> =_uploadUserInfo // lets make this generic to use it with all apis

    private val _uploadProfilePhoto = MutableLiveData<String?>() // lets make this generic to use it with all apis
    val uploadProfilePhoto :LiveData<String?> =_uploadProfilePhoto // lets make this generic to use it with all apis


    fun responseCodeDataSetter(responseBody : ResponseLoginPhone?) { // lets post this to our listener places
        this._responseDataCode.postValue(responseBody)
    }

    fun setConfirmCode(responseBody : ResponseBody?) { // lets post this to our listener places
        this._confirmCodeRes.postValue(responseBody)
    }
    fun setUploadRes(responseBody : ResponseBody?) { // lets post this to our listener places
        this._uploadUserInfo.postValue(responseBody)
    }

    fun postLoginPhone(requestLoginPhone: RequestLoginPhone) {
        setNetworkLoader(ShowProgress())
       viewModelScope.launch {
           val res = myRepo.loginPhoneNumber(requestLoginPhone){ response, errors->
               response?.let {it->
                   responseCodeDataSetter(it)
               }
               errors?.let {it->
                   SetError(it)
               }
               setNetworkLoader(DissMissProgress())

           }

           }
       }
    fun postOtpVerfiying(requestLoginPhone: RequestOtpVerfying) {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
          myRepo.OtpVerfyingPost(requestLoginPhone){ response, errors->
                response?.let {it->
                    setConfirmCode(it)
                }
                errors?.let {it->
                    SetError(it)
                }
                setNetworkLoader(DissMissProgress())

            }

        }
    }
    fun postUpdateProfileInfo(postProfile: PostUploadRequest) {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
            val res = myRepo.postUpdateInfo(postProfile){ response, errors->
                response?.let {it->
                    setUploadRes(it)
                }
                errors?.let {it->
                    SetError(it)
                }
                setNetworkLoader(DissMissProgress())

            }

        }
    }
    fun clearObserversListener(viewLifecycle: LifecycleOwner){
        //_responseDataCode.removeObservers(viewLifecycle)
       // _responseDataCode.postValue(null)
        clearAnyLiveData(viewLifecycle,_responseDataCode)
        clearAnyLiveData(viewLifecycle,_uploadProfilePhoto)
        clearAnyLiveData(viewLifecycle,_confirmCodeRes)
        clearAnyLiveData(viewLifecycle,_uploadUserInfo)

    }
    private fun clearAnyLiveData(
        viewLifeCycle: LifecycleOwner,
        responseDataLive: MutableLiveData<*>
    ){
        responseDataLive.removeObservers(viewLifeCycle)
        responseDataLive.postValue(null)
    }

    private fun clearAnyLiveData(
        onActivity: FragmentActivity,
        responseDataLive: MutableLiveData<*>
    ){
        responseDataLive.removeObservers(onActivity)
        responseDataLive.postValue(null)
    }
    fun clearObserver(onActivity: FragmentActivity){
        clearAnyLiveData(onActivity,_responseDataCode)
        clearAnyLiveData(onActivity,_confirmCodeRes)
        clearAnyLiveData(onActivity,_uploadUserInfo)

    }

    fun uploadProfileInfo(requestUploadGson: RequestUploadGsonObject, file: File) {
        setNetworkLoader(ShowProgress())

        viewModelScope.launch {
            // first get presigned url
            myRepo.uploadProfileImage(requestUploadGson) { success, errors ->
                success?.let { presignedCall ->

                    viewModelScope.launch {
                        // upload presigned url
                        uploadRepo.uploadAmazonLink(/*splitQuery(queryUrl)*/presignedCall.data[0],
                            "",file
                        ) { success, errors ->
                            success?.let { it ->
                                // lets call post faceverfication
                                viewModelScope.launch {
                                    // check presigned url image
                                    myRepo.postFaceVerfication(PostFaceVerficationRes(
                                        ArrayList<Datas>().also {
                                            it.add(Datas(presignedCall.data[0])) }
                                        )){ success, errors->
                                        success?.let {it->
                                            _uploadProfilePhoto.postValue(it.toString())
                                        }
                                        errors?.let {it->
                                            SetError(it)
                                        }
                                        setNetworkLoader(DissMissProgress())

                                    }
                                }
                            //    _uploadProfilePhoto.postValue(it)
                            }
                            errors?.let { it ->
                                setNetworkLoader(DissMissProgress())
                                SetError(it)
                            }
                        }
                    }
                }
                errors?.let { it ->
                    SetError(it)
                    setNetworkLoader(DissMissProgress())
                }
            }
        }

    }

}