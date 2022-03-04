package com.example.appgallery.introduction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appgallery.R
import com.example.appgallery.base.BaseActivity
import com.example.appgallery.container.ContainerActivity
import com.example.appgallery.databinding.ActivityIntroductionBinding
import com.example.appgallery.util.Util
import javax.inject.Inject

class IntroductionActivity : BaseActivity() {
    lateinit var binding : ActivityIntroductionBinding
    @Inject
    lateinit var util: Util
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.continueButton.setOnClickListener{
            startActivity(Intent(this,ContainerActivity::class.java))
        }
    }
}