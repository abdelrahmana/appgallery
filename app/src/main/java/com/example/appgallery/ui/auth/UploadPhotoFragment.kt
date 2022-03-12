package com.example.appgallery.ui.auth

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.appgallery.R
import com.example.appgallery.databinding.FragmentUploadPhotoBinding
import com.example.appgallery.util.NameUtil
import com.example.appgallery.util.Util
import com.example.appgallery.workmanger.model.Datax
import com.example.appgallery.workmanger.model.Dataxx
import com.example.appgallery.workmanger.model.RequestUploadGson
import com.example.appgallery.workmanger.model.RequestUploadGsonObject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UploadPhotoFragment : Fragment() {


    @Inject
    lateinit var util: Util

    @Inject
    lateinit var progressLoader : Dialog
    val viewModel : SignPhoneViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    lateinit var binding : FragmentUploadPhotoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUploadPhotoBinding.inflate(layoutInflater,container,false)
        checkPermssions()
        setObserverViewModel()
        binding.continueButton.setOnClickListener{
            checkPermssions()
        }
        return binding.root
    }

    private fun checkPermssions() {
        if (util.checkPermssionGrantedForImageAndFile(
                requireActivity(),
                Util.PERMSSIONS_FILES, accessPermssionCallBack
            )
        )
            util.performImgPicAction(NameUtil.CAMERA,null,requireActivity(),registerIntentResultCamera,
                null)
    }
    val accessPermssionCallBack =  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { permissions ->
        val granted =   permissions.entries.all {
            it.value == true
        }
        if (granted)
        // go to the validation fragment
            util.performImgPicAction(NameUtil.CAMERA,null,requireActivity(),registerIntentResultCamera,
                null)
        else
            util.showSnackMessages(activity, getString(R.string.cant_move_forward))

    }
    private fun setObserverViewModel() {

        viewModel.networkLoader.observe(viewLifecycleOwner, Observer{
            it?.let { progress->
                progress.setDialog(progressLoader) // open close principles
                viewModel.setNetworkLoader(null)
            }
        })

        viewModel.uploadProfilePhoto.observe(viewLifecycleOwner, Observer{
            if (it!=null){
                // here we can go to upload
                util.showSnackMessages(requireActivity(),getString(R.string.image_uploaded_successfully)
                    ,R.color.green)
            }
        })
        viewModel.errorViewModel.observe(viewLifecycleOwner, Observer{
            if (it!=null){
                util.showSnackMessages(requireActivity(),it)

            }
        })
    }

    override fun onDestroyView() {
        viewModel.clearObserversListener(viewLifecycleOwner)
        super.onDestroyView()
    }
    val registerIntentResultCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data

                    addImageToList( data!!.extras!!["data"] as Bitmap?)


            }
        }
    private fun addImageToList(bitmapUpdatedImage: Bitmap?) {
        val file =util.getCreatedFileFromBitmap("image",bitmapUpdatedImage!!,"jpg",requireContext())
        Glide.with(requireContext()).load(bitmapUpdatedImage)
            .error(R.color.gray).placeholder(R.color.gray).dontAnimate().into(binding.myPhoto)
        val requestUploadGson = RequestUploadGsonObject(
            Dataxx("jpg")
        )

        viewModel.uploadProfileInfo(requestUploadGson,file)

    }
}