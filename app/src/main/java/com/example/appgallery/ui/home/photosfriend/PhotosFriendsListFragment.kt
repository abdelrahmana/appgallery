package com.example.appgallery.ui.home.photosfriend

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
import com.example.appgallery.R
import com.example.appgallery.databinding.FragmentPhotosFriendsListBinding
import com.example.appgallery.ui.home.PhotosViewModel
import com.example.appgallery.ui.home.adapter.ClickAction
import com.example.appgallery.ui.home.adapter.PhotosRootAdapter
import com.example.appgallery.ui.home.model.Data
import com.example.appgallery.ui.home.model.Pagination
import com.example.appgallery.ui.home.model.PhotosArray
import com.example.appgallery.ui.home.photosuser.FriendsUserImplementer
import com.example.appgallery.ui.home.photosuser.PhotosUserFragment
import com.example.appgallery.ui.home.photosuser.PhotosUserImplementer
import com.example.appgallery.util.NestedScrollPaginationView
import com.example.appgallery.util.Util
import com.google.gson.Gson
import com.orwa.buysell.present.main.ad.videoimage.PopUpImageVideoPager
import com.orwa.buysell.present.main.ad.videoimage.PopUpImageVideoPager.Companion.MEDIA_LIST
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhotosFriendsListFragment : Fragment(), NestedScrollPaginationView.OnMyScrollChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    val viewModel : PhotosViewModel by viewModels()
    var showMore : Boolean? =false
    @Inject lateinit var progressDialog : Dialog
    @Inject lateinit var util: Util
    lateinit var binding : FragmentPhotosFriendsListBinding
    lateinit var adapter : PhotosRootAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPhotosFriendsListBinding.inflate(layoutInflater,container,false)
        adapter = PhotosRootAdapter(imageClickedDetails,showMoreImagesClicked,ArrayList(),util)
        binding.nestedScrollPagination.myScrollChangeListener = this
        adapter.arrayListOfImagessValues.clear()
        callGetMorePhotos(HashMap<String, Any>().also {
            it.put("photoscount",4)
            it.put("pagination",Gson().toJson(Pagination("0",3)))
        })
        setUpRootRecycleView(adapter)
        setObserverViewModel()
        return binding.root
    }

    private fun callGetMorePhotos(hashMap: HashMap<String, Any>) {
        viewModel.getPhotosListOfFriends(hashMap)
    }

    private fun setUpRootRecycleView(adapter: PhotosRootAdapter) {
        util.setRecycleView(binding.parentRecycle,adapter,LinearLayoutManager.VERTICAL,requireContext(),null,false)
    }

    val showMoreImagesClicked :(Data)->Unit = {clicked->
        val bundle = bundleOf(
            PhotosUserFragment.IDOFUSER to clicked.taggedUserId,
            PhotosUserFragment.USER_NAME to clicked.taggedUser
        )
        util.changeFragmentBack(requireActivity(), PhotosUserFragment(FriendsUserImplementer()),
            "photos_friends",bundle, R.id.fragment_container_navigation)
    }
    val imageClickedDetails :(ClickAction)->Unit = { clicked->
        val bundle = bundleOf(PopUpImageVideoPager.MEDIA_LIST to Gson().toJson(clicked.item)
            ,PopUpImageVideoPager.SELECTEDIMAGE to clicked.selectedPosition)
        PopUpImageVideoPager().also { it.arguments=bundle }.
        show(activity?.supportFragmentManager?.beginTransaction()!!,"POPUP_photos")
    }
    private fun setObserverViewModel() {
        viewModel.responsePhotosList.observe(viewLifecycleOwner, Observer{
            if (it!=null){
                showMore = it.isNotEmpty()
                adapter.updateList(it)
                if (adapter.arrayListOfImagessValues.isEmpty())
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
                adapter.arrayListOfImagessValues.
                get(adapter.arrayListOfImagessValues.size-1)/*.photosArray*/.also {
                    lastItemId = it.lastId.toString()//it.get(it.size-1).id
                }
                it.put("photoscount",4)
                it.put("pagination",Gson().toJson(Pagination(lastItemId,5)))
            })
          //  viewModel.getPhotosListOfFriends()
    }
}