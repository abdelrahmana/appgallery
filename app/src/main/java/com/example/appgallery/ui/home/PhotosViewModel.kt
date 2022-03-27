package com.example.appgallery.ui.home

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.appgallery.base.BaseViewModel
import com.example.appgallery.repo.HomeRepo
import com.example.appgallery.repo.UploadRepo
import com.example.appgallery.ui.home.model.Data
import com.example.appgallery.ui.home.model.PhotosArray
import com.example.appgallery.ui.home.photosuser.FriendsUserImplementer
import com.example.appgallery.ui.home.photosuser.PhotosUserImplementer
import com.example.appgallery.util.DissMissProgress
import com.example.appgallery.util.ShowProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// this viewpager is used within fragment
// specifically when running new fragment and notifying fragment with this change
@HiltViewModel
class PhotosViewModel @Inject constructor(private val myRepo: HomeRepo,
                                          private val uploadRepo: UploadRepo): BaseViewModel() {
   private val _responsePhotosList = MutableLiveData<List<Data>?>() // lets make this generic to use it with all apis
   val responsePhotosList :LiveData<List<Data>?> =_responsePhotosList // lets make this generic to use it with all apis
    private val _responsePhotosOfMeList = MutableLiveData<List<Data>?>() // lets make this generic to use it with all apis
    val responsePhotosOfMeList :LiveData<List<Data>?> =_responsePhotosOfMeList
  //  private val _confirmCodeRes = MutableLiveData<ResponseBody?>() // lets make this generic to use it with all apis
   // val confirmCodeLive :LiveData<ResponseBody?> =_confirmCodeRes // lets make this generic to use it with all apis

    private val _responseListLiveData = MutableLiveData<List<PhotosArray>?>() // lets make this generic to use it with all apis
    val responseListLiveData :LiveData<List<PhotosArray>?> =_responseListLiveData

    fun responseCodeDataSetter(responseBody : List<Data>?) { // lets post this to our listener places
        this._responsePhotosList.postValue(responseBody)
    }


    fun getPhotosListOfFriends(headerHashMap: HashMap<String,Any>) {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
         myRepo.getPhotosFriendsList(headerHashMap){ response, errors->
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
    fun getPhotosUserOfFriend(headerHashMap: HashMap<String,Any>) { // get photo user of friend
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
            myRepo.getPhotosUser(headerHashMap,FriendsUserImplementer()){ response, errors->
                response?.let {it->
                    _responseListLiveData.postValue(it)
                }
                errors?.let {it->
                    SetError(it)
                }
                setNetworkLoader(DissMissProgress())

            }

        }
    }
    fun getPhotosOfMeUser(headerHashMap: HashMap<String,Any>) { // get photo user of friend
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
            myRepo.getPhotosUser(headerHashMap, PhotosUserImplementer()){ response, errors->
                response?.let {it->
                    _responseListLiveData.postValue(it)
                }
                errors?.let {it->
                    SetError(it)
                }
                setNetworkLoader(DissMissProgress())

            }

        }
    }

    fun getPhotosOfMeResponse(headerHashMap: HashMap<String,Any>) {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
            myRepo.getPhotosOfMeList(headerHashMap){ response, errors->
                response?.let {it->
                    _responsePhotosOfMeList.postValue(it)
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
        clearAnyLiveData(viewLifecycle,_responsePhotosList)
        clearAnyLiveData(viewLifecycle,_responsePhotosOfMeList)
        clearAnyLiveData(viewLifecycle,_responseListLiveData)


        //    clearAnyLiveData(viewLifecycle,_uploadUserInfo)

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
        clearAnyLiveData(onActivity,_responsePhotosList)


    }

}