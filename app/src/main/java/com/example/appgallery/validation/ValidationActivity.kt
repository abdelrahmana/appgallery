package com.example.appgallery.validation

import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.appgallery.R
import com.example.appgallery.base.BaseActivity
import com.example.appgallery.util.Util
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ValidationActivity : BaseActivity() {

    @Inject
    lateinit var util: Util
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validation)
         setValidationFragment()

    }
    private fun setValidationFragment() {
         // phone is snet in util as prefs
      /*  val whichFragment = intent.getStringExtra(AuthContainer.whichFragment)?:AuthContainer.loginFragment
        val validation = ValidatePhoneFragment()
        val bundle = Bundle()
        bundle.putString(AuthContainer.whichFragment,whichFragment)
        validation.arguments = bundle*/
        util.changeFragment(ValidationFragment().also {
            val bundle = bundleOf(REQUESTOBJECT to intent.getStringExtra(REQUESTOBJECT),
                COUNTRYCODE to (intent.getStringExtra(COUNTRYCODE)?:""),
                PHONE to (intent.getStringExtra(PHONE)?:""))
                                                      it.arguments = bundle
        },supportFragmentManager, R.id.containerValidation)
    }
    companion object {
        val REQUESTOBJECT = "REQUEST"
        val PHONE = "PHONE"
        val COUNTRYCODE = "COUNTRY"
    }
}
