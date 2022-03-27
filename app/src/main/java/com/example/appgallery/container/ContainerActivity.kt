package com.example.appgallery.container

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appgallery.R
import com.example.appgallery.base.BaseActivity
import com.example.appgallery.board.VideoBoardFragment
import com.example.appgallery.ui.auth.UploadPhotoFragment
import com.example.appgallery.ui.auth.UploadUserNameFragment
import com.example.appgallery.util.Util
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContainerActivity(/*val interfaceItem : InterfaceRedirections*/) : BaseActivity() {
    @Inject
    lateinit var util:Util
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        var interfaces : InterfaceRedirections = if (intent.getIntExtra(ACTION,0)==0)
            VideoPageImplementer(VideoBoardFragment()) else if (intent.getIntExtra(ACTION,0)==1)
                UploadPhotoImplementer(UploadUserNameFragment()) else
             UplodUserName(UploadPhotoFragment())
        util.changeFragment(interfaces.getCurrentFragmnet(),supportFragmentManager,R.id.containerFragment)
    }
    override fun onBackPressed() {

        val count = supportFragmentManager.backStackEntryCount

        if (count > 1) {
            val f = supportFragmentManager.findFragmentById(R.id.containerFragment)

            supportFragmentManager.popBackStack() // go to login
        }else {
            //  startActivity(Intent(this,Introduction::class.java))
            finish()
            //super.onBackPressed()
        }

    }
    companion object {
        val ACTION = "ACTION"
    }
}