package com.example.appgallery.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import com.example.appgallery.base.BaseFragment
import com.example.appgallery.databinding.FragmentPhoneBinding
import com.example.appgallery.util.Util
import com.example.negotation.auth.LoginFragment
import com.example.speedone.R
import com.example.speedone.databinding.FragmentPhoneBinding
import com.example.speedone.util.NameUtil
import com.example.speedone.util.NameUtil.COUNTRY_CODE
import com.example.speedone.util.NameUtil.FORGETPASSWORD
import com.example.speedone.util.NameUtil.LOGINCASE
import com.example.speedone.util.NameUtil.PHONE
import com.example.speedone.util.UtilKotlin
import com.example.speedone.util.Utilii
import com.example.speedone.validation.ValidatePhoneFragment
import com.example.speedone.validation.ValidationActivity
import com.seven.util.PrefsUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class SignPhoneFragment : BaseFragment() {
    @Inject
    lateinit var util: Util
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
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

    }

    private fun setClickListener() {
        binding.nextButton.setOnClickListener{
           if (util.checkAvalibalityOptions(binding.phoneEditText.text.toString())==true)
               // not empty we have to set avaliablity option
             /*  PrefsUtil.getSharedPrefs(context!!).edit().putString(ValidatePhoneFragment.phoneNumberKey,
                   /*binding.startPrefx.text.toString()
                       +*/binding.phoneEditText.text.toString())
                   .putString(ValidatePhoneFragment.PHONEKEY,binding.startPrefx.text.toString()).apply()*/
                //   startForResult.launch(Intent(activity, ValidationActivity::class.java))


        }
    }
    // start activity for result
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            //  you will get result here in result.data
            val bundle = bundleOf(
                PHONE to binding.phoneEditText.text.trim().toString(),
                COUNTRY_CODE to binding.ccp.selectedCountryCode

            ) // transfer item for usage

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