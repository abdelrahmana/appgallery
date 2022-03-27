package com.example.appgallery.ui.home.photosuser

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgallery.databinding.PhotosOfMeListBinding
import com.example.appgallery.ui.home.PhotosViewModel
import com.example.appgallery.ui.home.adapter.ClickAction
import com.example.appgallery.ui.home.adapter.PhotosOfMeRootAdapter
import com.example.appgallery.ui.home.model.Data
import com.example.appgallery.ui.home.model.Pagination
import com.example.appgallery.ui.home.model.PhotosArray
import com.example.appgallery.ui.home.photosuser.adapter.PhotosUserAdapter
import com.example.appgallery.util.GridModel
import com.example.appgallery.util.NestedScrollPaginationView
import com.example.appgallery.util.Util
import com.google.gson.Gson
import com.orwa.buysell.present.main.ad.videoimage.PopUpImageVideoPager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhotosUserFragment(val photoInterface:PhotosOperationInterface) : Fragment(), NestedScrollPaginationView.OnMyScrollChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    val viewModel : PhotosViewModel by viewModels()
    var showMore : Boolean? =false
    @Inject lateinit var progressDialog : Dialog
    @Inject lateinit var util: Util
    lateinit var binding : PhotosOfMeListBinding
    var arrayListValues = ArrayList<PhotosArray>()
    lateinit var adapter : PhotosUserAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = PhotosOfMeListBinding.inflate(layoutInflater,container,false)
        adapter = PhotosUserAdapter(arrayListValues,imageClickedDetails)
        binding.nestedScrollPagination.myScrollChangeListener = this
        setRecycleViewIAdapter(adapter)
        callGetMorePhotos(HashMap<String, Any>().also {
            it.put(photoInterface.getUploaderKey(),arguments?.getString(IDOFUSER)?:"")
            it.put("pagination",Gson().toJson(Pagination("0",10)))
        })
        setObserverViewModel()
        // set UserName
        binding.userName.text = arguments?.getString(USER_NAME)?:""
        return binding.root
    }

    private fun setRecycleViewIAdapter(adapter: PhotosUserAdapter) {
        util.setRecycleView(binding.parentRecycle,adapter,null,requireContext(), GridModel(2,15),false)
    }

    private fun callGetMorePhotos(hashMap: HashMap<String, Any>) {
        photoInterface.setViewModelCall(hashMap,viewModel)
    //    viewModel.getPhotosOfMeUser(hashMap)
    }
   /* val showMoreImagesClicked :(Data)->Unit = {clicked->

    }*/
    val imageClickedDetails :(ClickAction)->Unit = { clicked->
        val bundle = bundleOf(PopUpImageVideoPager.MEDIA_LIST to Gson().toJson(clicked.item)
        ,PopUpImageVideoPager.SELECTEDIMAGE to clicked.selectedPosition)
        PopUpImageVideoPager().also { it.arguments=bundle }.
        show(activity?.supportFragmentManager?.beginTransaction()!!,"POPUP_photos")
    }
    private fun setObserverViewModel() {
        viewModel.responseListLiveData.observe(viewLifecycleOwner, Observer{
            if (it!=null){
                showMore = it.isNotEmpty()
                adapter.updateList(/*if(showMore==true)it else arrayListValues*/it)
                if (adapter.arrayList.isEmpty())
                    binding.noResult.visibility = View.VISIBLE

            }
        })

        viewModel.networkLoader.observe(viewLifecycleOwner,Observer{
            it?.let { progress->
                progress.setDialog(progressDialog) // open close principles

            }
        })

        viewModel.errorViewModel.observe(viewLifecycleOwner, Observer{
            it?.let { errorMessage->
                // progress.setDialog(progressDialog) // open close principles
                util.showSnackMessages(requireActivity(),errorMessage)

            }
        })
    }
    override fun onDestroyView() {
        viewModel.clearObserversListener(viewLifecycleOwner)
        super.onDestroyView()
    }

    override fun onLoadMore(currentPage: Int) {
        if (showMore == true)
            callGetMorePhotos(HashMap<String, Any>().also {
                var lastItemId = "0"
                adapter.arrayList.
                get(adapter.arrayList.size-1).also {
                    lastItemId = it.id
                }
                it.put(photoInterface.getUploaderKey(),arguments?.getString(IDOFUSER)?:"")
                it.put("pagination",Gson().toJson(Pagination(lastItemId,10)))
            })
    }
    companion object {
        val IDOFUSER = "ID_USER"
        val USER_NAME = "USER_NAME"
    }
}