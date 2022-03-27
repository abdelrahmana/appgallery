package com.example.appgallery.ui.auth

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appgallery.R
import com.example.appgallery.databinding.FragmentUploadPhotoBinding
import com.example.appgallery.ui.home.HomeActivityBottomNav
import com.example.appgallery.util.NameUtil
import com.example.appgallery.util.Util
import com.example.appgallery.workmanger.model.Dataxx
import com.example.appgallery.workmanger.model.RequestUploadGsonObject
import com.seven.util.PrefsUtil
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.net.URI
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

    var photoUri : Uri?=null
    private fun checkPermssions() {
        if (util.checkPermssionGrantedForImageAndFile(
                requireActivity(),
                Util.PERMSSIONS_FILES, accessPermssionCallBack
            )
        )
            photoUri=   util.performImgPicAction(NameUtil.CAMERA,null,requireActivity(),registerIntentResultCamera,
                null)
    }
    val accessPermssionCallBack =  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { permissions ->
        val granted =   permissions.entries.all {
            it.value == true
        }
        if (granted)
        // go to the validation fragment
            photoUri =    util.performImgPicAction(NameUtil.CAMERA,null,requireActivity(),registerIntentResultCamera,
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
                // image uploaded successfully
                val newUpdate =   PrefsUtil().getUserModel(requireContext()).also {
                    it?.data?.registrationStepId = 3
                }
                PrefsUtil().setLoginModel(requireContext(),newUpdate)
                startActivity(Intent(requireContext(),HomeActivityBottomNav::class.java))
                requireActivity().finish()
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

                 //   addImageToList( data!!.extras!!["data"] as Bitmap?)

                photoUri?.let { it->
                    binding.myPhoto.setImageURI(it)
                    val requestUploadGson = RequestUploadGsonObject(
                        Dataxx("jpg")
                    )
                    val stream = requireActivity().contentResolver.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(stream)
                    val file =util.getCreatedFileFromBitmap("image",
                        bitmap,"jpg",requireContext())

                    viewModel.uploadProfileInfo(requestUploadGson, file)
                }
            //    binding.myPhoto.setImageBitmap(data.extras!!["data"] as Bitmap?)

            }
        }
    fun resizeBitMap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        val ratioX = newWidth / bitmap.width.toFloat()
        val ratioY = newHeight / bitmap.height.toFloat()
        val middleX = newWidth / 2.0f
        val middleY = newHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bitmap,
            middleX - bitmap.width / 2,
            middleY - bitmap.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
        return scaledBitmap
    }
    private fun getScaledBitMapBaseOnScreenSize(bitmapOriginal: Bitmap): Bitmap? {
        var scaledBitmap: Bitmap? = null
        try {
            val metrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
            val width = bitmapOriginal.width
            val height = bitmapOriginal.height
            val scaleWidth = metrics.scaledDensity
            val scaleHeight = metrics.scaledDensity

            // create a matrix for the manipulation
            val matrix = Matrix()
            // resize the bit map
            matrix.postScale(scaleWidth, scaleHeight)

            // recreate the new Bitmap
            scaledBitmap = Bitmap.createBitmap(bitmapOriginal, 0, 0, width, height, matrix, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return scaledBitmap
    }
    private fun addImageToList(bitmapUpdatedImage: Bitmap?) {

        val file =util.getCreatedFileFromBitmap("image",bitmapUpdatedImage!!,"jpg",requireContext())
     /*   Glide.with(requireContext()).load(bitmapUpdatedImage)
            .error(R.color.gray).placeholder(R.color.gray).dontAnimate().into(binding.myPhoto)*/
        val resizedBitmap: Bitmap = Bitmap.createScaledBitmap(bitmapUpdatedImage,1000, 1000, true);

        binding.myPhoto.setImageBitmap(getScaledBitMapBaseOnScreenSize(bitmapUpdatedImage))
            //.setImageBitmap(resizeBitMap(bitmapUpdatedImage,binding.container.width,binding.container.height))
        val requestUploadGson = RequestUploadGsonObject(
            Dataxx("jpg")
        )

        viewModel.uploadProfileInfo(requestUploadGson,file)

    }
}