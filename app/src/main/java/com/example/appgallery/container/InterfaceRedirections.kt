package com.example.appgallery.container

import androidx.fragment.app.Fragment

interface InterfaceRedirections {
    fun getCurrentFragmnet() : Fragment
}

class VideoPageImplementer(val fragment: Fragment) :InterfaceRedirections{
    override fun getCurrentFragmnet(): Fragment {
        return fragment
    }

}
class UplodUserName(val fragment: Fragment) :InterfaceRedirections{
    override fun getCurrentFragmnet(): Fragment {
        return fragment
    }

}
class UploadPhotoImplementer(val fragment: Fragment) :InterfaceRedirections{
    override fun getCurrentFragmnet(): Fragment {
        return fragment
    }

}