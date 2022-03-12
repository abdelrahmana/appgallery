package com.example.appgallery.ui.permisson

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.appgallery.R
import com.example.appgallery.base.BaseFragment
import com.example.appgallery.container.ContainerActivity
import com.example.appgallery.databinding.FragmentPermissionBinding
import com.example.appgallery.ui.auth.SignPhoneFragment
import com.example.appgallery.util.Util
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import javax.inject.Inject
@AndroidEntryPoint
class PermissionFragment : BaseFragment() {
    @Inject
    lateinit var util: Util
    lateinit var binding :FragmentPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPermissionBinding.inflate(layoutInflater,container,false)
        checkPermssions()
        binding.permssionsButton.setOnClickListener{
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
            util.changeFragmentBack(requireActivity(),SignPhoneFragment(),"permission",null,R.id.containerFragment)
     // startActivity(Intent(requireContext(),ContainerActivity::class.java))
    }

    val accessPermssionCallBack =  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { permissions ->
        val granted =   permissions.entries.all {
            it.value == true
        }
        if (granted)
            // go to the validation fragment
            util.changeFragmentBack(requireActivity(),SignPhoneFragment(),"permission",null,R.id.containerFragment)
        else
            util.showSnackMessages(activity, getString(R.string.cant_move_forward))

    }

}