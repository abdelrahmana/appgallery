package com.example.appgallery.validation

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appgallery.base.BaseFragment
import com.example.appgallery.databinding.FragmentValidationBinding
import com.example.appgallery.ui.auth.SignPhoneViewModel
import com.example.appgallery.ui.auth.model.OtpVerfyingData
import com.example.appgallery.ui.auth.model.RequestOtpVerfying
import com.example.appgallery.util.Util
import com.google.gson.Gson
import com.mukesh.OnOtpCompletionListener
import com.mukesh.OtpView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ValidationFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    @Inject
    lateinit var progressDialog : Dialog
    @Inject lateinit var util : Util
    val viewModel : SignPhoneViewModel by viewModels()
    lateinit var binding : FragmentValidationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      binding = FragmentValidationBinding.inflate(layoutInflater,container,false)
        setOtpCompleted(binding.loginPhoneEdit)
        binding.nextButton.setOnClickListener{
            if(util.checkAvalibalityOptions(binding.loginPhoneEdit.text.toString())==true &&
                binding.loginPhoneEdit.text.toString().length ==6)
                viewModel.postOtpVerfiying(RequestOtpVerfying(OtpVerfyingData
                    ("+"+arguments?.getString(ValidationActivity.COUNTRYCODE)?:"",
                    arguments?.getString(ValidationActivity.PHONE)?:"",
                    binding.loginPhoneEdit.text.toString())))

        }
        setObserverViewModel()
        return binding.root
    }

    private fun setObserverViewModel() {
        viewModel.networkLoader.observe(viewLifecycleOwner, Observer{
            it?.let { progress->
                progress.setDialog(progressDialog) // open close principles
                viewModel.setNetworkLoader(null)
            }
        })

        viewModel.confirmCodeLive.observe(viewLifecycleOwner, Observer{
            if (it!=null){
                val data = Intent()
                // return sign in phone success
                requireActivity().setResult(Activity.RESULT_OK, data)
                requireActivity().finish() // this

            }
        })
        viewModel.errorViewModel.observe(viewLifecycleOwner, Observer{
            if (it!=null){
                util.showSnackMessages(requireActivity(),it)

            }
        })
    }
    private fun setOtpCompleted(otpView: OtpView) {
        otpView.setOtpCompletionListener { otp ->
            viewModel.postOtpVerfiying(RequestOtpVerfying(OtpVerfyingData
                ("+"+arguments?.getString(ValidationActivity.COUNTRYCODE)?:"",
                arguments?.getString(ValidationActivity.PHONE)?:"",otp)))

        }
    }

}