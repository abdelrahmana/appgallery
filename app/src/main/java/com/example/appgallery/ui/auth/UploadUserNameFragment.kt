package com.example.appgallery.ui.auth

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appgallery.R
import com.example.appgallery.databinding.FragmentUploadUserNameBinding
import com.example.appgallery.ui.auth.model.DataProfile
import com.example.appgallery.ui.auth.model.PostUploadRequest
import com.example.appgallery.util.Util
import com.example.appgallery.validation.ValidationActivity
import com.google.gson.Gson
import com.seven.util.PrefsUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UploadUserNameFragment : Fragment() {


    @Inject
    lateinit var util : Util
   // @Inject
  //  lateinit var prefUtil : PrefsUtil
    @Inject
    lateinit var progressDialog : Dialog
    val viewModel : SignPhoneViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    lateinit var binding : FragmentUploadUserNameBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUploadUserNameBinding.inflate(layoutInflater,container,false)
        setOnClickListenerInput()
        setObserverViewModel()
        return binding.root
    }

    private fun setOnClickListenerInput() {
        binding.continueButton?.setOnClickListener{
           if (util.checkAvalibalityOptions(binding.userNameEditText.text.toString())==true)
            viewModel.postUpdateProfileInfo(PostUploadRequest(DataProfile
                (name = binding.userNameEditText.text.toString())))
            else
                util.showSnackMessages(requireActivity(),getString(R.string.please_enter_name))
        }
    }

    private fun setObserverViewModel() {
        viewModel.networkLoader.observe(viewLifecycleOwner, Observer{
            it?.let { progress->
                progress.setDialog(progressDialog) // open close principles
                viewModel.setNetworkLoader(null)
            }
        })

        viewModel.uploadUserInfo.observe(viewLifecycleOwner, Observer{
            if (it!=null){
                // now everything setted up
              val newUpdate =   PrefsUtil().getUserModel(requireContext()).also {
                    it?.data?.registrationStepId = 2
                }
                PrefsUtil().setLoginModel(requireContext(),newUpdate)
                util.showSnackMessages(requireActivity(),
                    getString(R.string.personal_info_updated_successfully),R.color.green)
                util.changeFragmentBack(requireActivity(),UploadPhotoFragment(),"uploadUserName",null
                    ,R.id.containerFragment)

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

}