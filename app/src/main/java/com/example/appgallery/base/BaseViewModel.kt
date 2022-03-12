package com.example.appgallery.base

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appgallery.util.ProgressDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped

open class BaseViewModel : ViewModel() {
    private val _errorViewModel = MutableLiveData<String?>()
    val errorViewModel :LiveData<String?> = _errorViewModel

    private val _responseDataCodeLocation = MutableLiveData<Location?>()
    val responseDataCodeLocation :LiveData<Location?> = _responseDataCodeLocation

    fun setResponseDataLocation(location : Location?) {
        _responseDataCodeLocation.postValue(location)
    }
    private val _networkLoader = MutableLiveData<ProgressDialog?>()
    val networkLoader :LiveData<ProgressDialog?> = _networkLoader

    fun SetError(error : String?) {
       _errorViewModel.postValue(error)
    }

    fun setNetworkLoader(loader : ProgressDialog?) {
        _networkLoader.postValue(loader)
    }
}