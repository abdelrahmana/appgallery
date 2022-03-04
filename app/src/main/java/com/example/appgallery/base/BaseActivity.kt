package com.example.appgallery.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appgallery.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
}