package com.example.appgallery.ui.auth

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appgallery.R
import com.example.appgallery.base.BaseFragment
import com.example.appgallery.container.ContainerActivity
import com.example.appgallery.databinding.FragmentPhoneBinding
import com.example.appgallery.ui.auth.model.Data
import com.example.appgallery.ui.auth.model.RequestLoginPhone
import com.example.appgallery.ui.auth.model.ResponseLoginPhone
import com.example.appgallery.ui.home.HomeActivityBottomNav
import com.example.appgallery.util.Util
import com.example.appgallery.validation.ValidationActivity
import com.example.appgallery.validation.ValidationActivity.Companion.PHONE
import com.example.appgallery.validation.ValidationActivity.Companion.REQUESTOBJECT
import com.google.gson.Gson
import com.seven.util.PrefsModel
import com.seven.util.PrefsUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignPhoneFragment : BaseFragment() {
    @Inject
    lateinit var util: Util
    @Inject
    lateinit var progressDialog : Dialog
    @Inject
    lateinit var prefUtil : PrefsUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    val viewModel : SignPhoneViewModel by viewModels()
    lateinit var binding : FragmentPhoneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPhoneBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        setObserverViewModel()

    }
    var currentPhone = ""
    private fun setClickListener() {
        binding.nextButton.setOnClickListener{
            currentPhone = if (binding.phoneEditText.text.trim().startsWith("0"))
                binding.phoneEditText.text.trim().toString().substring(1)
            else
                binding.phoneEditText.text.trim().toString()
           if (util.checkAvalibalityOptions(binding.phoneEditText.text.toString())==true)
               viewModel.postLoginPhone(RequestLoginPhone(Data("+"+binding.ccp.selectedCountryCode,
                   "2",
                   util.getDeviceId(),/*binding.phoneEditText.text.trim().toString()*/currentPhone
                   )))
               // not empty we have to set avaliablity option
             /*  PrefsUtil.getSharedPrefs(context!!).edit().putString(ValidatePhoneFragment.phoneNumberKey,
                   /*binding.startPrefx.text.toString()
                       +*/binding.phoneEditText.text.toString())
                   .putString(ValidatePhoneFragment.PHONEKEY,binding.startPrefx.text.toString()).apply()*/
                //   startForResult.launch(Intent(activity, ValidationActivity::class.java))


        }
    }
    var responseLoginPhone : ResponseLoginPhone? =null
    private fun setObserverViewModel() {

        viewModel.networkLoader.observe(viewLifecycleOwner, Observer{
            it?.let { progress->
                progress.setDialog(progressDialog) // open close principles
                viewModel.setNetworkLoader(null)
            }
        })

        viewModel.responseDataLive.observe(viewLifecycleOwner, Observer{
            if (it!=null){
                responseLoginPhone = it
                startForResult.launch(Intent(activity, ValidationActivity::class.java).putExtra(REQUESTOBJECT,
                    Gson().toJson(it))
                    .putExtra(
                        ValidationActivity.PHONE,
                        /*binding.phoneEditText.text.toString()*/currentPhone)
                    .putExtra(
                        ValidationActivity.COUNTRYCODE,
                        binding.ccp.selectedCountryCode))
                // success so we need to go to home
                // now we need to go forward to next fragment which is used for send data

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

    // start activity for result
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            //  you will get result here in result.data
            val bundle = bundleOf(
                PHONE to /*binding.phoneEditText.text.trim().toString()*/currentPhone,
                COUNTRY_CODE to binding.ccp.selectedCountryCode
            // please get the token
             // set the shared preferences here
            ) // transfer item for usage
            prefUtil.setUserToken(requireContext(),responseLoginPhone?.data?.token?:"")
            prefUtil.setLoginState(requireContext(),true)
            prefUtil.setLoginModel(requireContext(),responseLoginPhone)
            var intentRedirections : Intent? =null
            if (responseLoginPhone?.data?.registrationStepId==1) // need to write name and pass
              /*  util.changeFragmentBack(requireActivity(),UploadUserNameFragment(),"sign_phone",bundle,
                    R.id.containerFragment)*/
                intentRedirections = Intent(requireContext(),
                    ContainerActivity(/*UploadPhotoImplementer(UploadUserNameFragment())*/)::class.java)
                    .putExtra(
                    ContainerActivity.ACTION,1)


            else if (responseLoginPhone?.data?.registrationStepId ==2) // need to upload his photo
              /*  util.changeFragmentBack(requireActivity(),UploadPhotoFragment(),"sign_phone",bundle,
                    R.id.containerFragment)*/
                intentRedirections = Intent(requireContext(),
                    ContainerActivity(/*UploadPhotoImplementer(UploadUserNameFragment())*/)::class.java)
                    .putExtra(
                        ContainerActivity.ACTION,2)
                   else //normal logged in
                intentRedirections = Intent(requireActivity(),HomeActivityBottomNav::class.java)
                       startActivity(intentRedirections)
            activity?.finish()
         //   util.changeFragmentBack(requireActivity(),)

        }
    }
   /*     // check phone in case it is not empty
        private fun observerCheckPhoneNumber(): DisposableObserver<Response<CheckPhoneResponse>> {

            return object : DisposableObserver<Response<CheckPhoneResponse>>() {
                override fun onComplete() {
                    progressDialog?.dismiss()
                    dispose()
                }

                override fun onError(e: Throwable) {
                    UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                    progressDialog?.dismiss()
                    dispose()
                }

                override fun onNext(response: Response<CheckPhoneResponse>) {
                    progressDialog?.dismiss()
                    if (response!!.isSuccessful) {
                        if (response.body()?.content?.data?.data?.phone == true) {

                            // start activity for result here
                            UtilKotlin.startValidationFragmentForResult(
                                this@FragmentRegisterationCustomer,
                                101,
                                activity!!,
                                editTextPhone?.text.toString()
                            )

                        } else {
                            // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                            //   val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                            UtilKotlin.showSnackErrorInto(activity!!, getString(R.string.errors))

                        }
                    }/* else
                    UtilKotlin.showSnackErrorInto(activity!!, response.body()?.message?:"")*/
                    else {
                        if (response.errorBody() != null) {
                            // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                            val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                            UtilKotlin.showSnackErrorInto(activity!!, error)
                        }

                    }
                }

        }

    }*/
    companion object {
       val TYPE_USAGE = "TYPE_VALIDATION"
        val FOR_REGISTERATION = "FOR_REGISTERATION"
        val PHONE = "phone"
        val COUNTRY_CODE = "country_code"
      //  val FOR_RESETPASS = "FOR_REST_PASS"
    }
}