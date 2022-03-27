package com.example.appgallery.introduction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appgallery.R
import com.example.appgallery.base.BaseActivity
import com.example.appgallery.board.VideoBoardFragment
import com.example.appgallery.container.ContainerActivity
import com.example.appgallery.container.ContainerActivity.Companion.ACTION
import com.example.appgallery.container.UploadPhotoImplementer
import com.example.appgallery.container.UplodUserName
import com.example.appgallery.container.VideoPageImplementer
import com.example.appgallery.databinding.ActivityIntroductionBinding
import com.example.appgallery.ui.auth.UploadPhotoFragment
import com.example.appgallery.ui.auth.UploadUserNameFragment
import com.example.appgallery.ui.home.HomeActivityBottomNav
import com.example.appgallery.util.Util
import com.seven.util.PrefsUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroductionActivity : BaseActivity() {
    lateinit var binding : ActivityIntroductionBinding
    @Inject
    lateinit var util: Util
    @Inject
    lateinit var prefs : PrefsUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        var intentRedirections = Intent(this,
            ContainerActivity(/*VideoPageImplementer(VideoBoardFragment())*/)::class.java)
        setContentView(binding.root)
        if (prefs.isLoggedIn(this)){
           if (prefs.getUserModel(this)?.data?.registrationStepId == 3)
               intentRedirections = Intent(this,HomeActivityBottomNav::class.java)
           else if(prefs.getUserModel(this)?.data?.registrationStepId == 2) { // need to upload photo
               intentRedirections = Intent(this,
                   ContainerActivity(/*UplodUserName(UploadPhotoFragment())*/)::class.java)
                   .putExtra(ACTION,2)
           }
           else if(prefs.getUserModel(this)?.data?.registrationStepId == 1) {
               intentRedirections = Intent(this,
                   ContainerActivity(/*UploadPhotoImplementer(UploadUserNameFragment())*/)::class.java)
                   .putExtra(ACTION,1)
           }
            startActivity(intentRedirections)
            finish()
        }
        binding.continueButton.setOnClickListener{
            val intent = Intent(this,
                ContainerActivity(/*VideoPageImplementer(VideoBoardFragment())*/)::class.java).putExtra(ACTION,0)
            startActivity(intent)
            this.finish()
        }
    }
}