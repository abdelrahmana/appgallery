package com.example.appgallery.board

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.appgallery.R
import com.example.appgallery.base.BaseFragment
import com.example.appgallery.databinding.FragmentVideoBoardBinding
import com.example.appgallery.ui.permisson.PermissionFragment
import com.example.appgallery.util.Util
import javax.inject.Inject

class VideoBoardFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    @Inject
    lateinit var util: Util
    lateinit var binding : FragmentVideoBoardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVideoBoardBinding.inflate(layoutInflater,container,false)
        binding.skipContainer.setOnClickListener{
            moveFrowardSkip()
        }
        setCountDownTimer()
        return binding.root
    }

    private fun moveFrowardSkip() {
        if (context?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED &&
            context?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED &&
            context?.checkSelfPermission(Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED )
                // now we need to move forward to sign up

                    else
                        util.changeFragmentBack(requireActivity(),PermissionFragment(),VIDEOFRAG,null
                            ,R.id.containerFragment)

    }

    var countDownTimer : CountDownTimer?=null
    fun setCountDownTimer(){
        countDownTimer = object : CountDownTimer(10000,1000) {

            override fun onTick(p0: Long) {
                Log.v("Log_tag", "Tick of Progress"+ p0)
                val progress = (p0 / 100)
                binding.progressLoader.setProgressCompat(progress.toInt(),true)

                // binding.progressLoader.progress = progress.toInt()
            }

            override fun onFinish() {
                binding.progressLoader.progress = 0
                moveFrowardSkip()

            }
        };
        countDownTimer?.start()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    companion object {
        val VIDEOFRAG = "VIDEOFRAG"
    }
}