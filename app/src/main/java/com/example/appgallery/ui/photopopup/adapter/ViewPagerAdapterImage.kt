package com.orwa.buysell.present.main.ad.videoimage.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appgallery.ui.home.model.PhotosArray
import com.google.gson.Gson
import com.orwa.buysell.present.main.ad.videoimage.PopUpImageVideoPager
import com.orwa.buysell.present.main.ad.videoimage.itempager.PopUPServiceDialog

class ViewPagerAdapterImage(fragment: Fragment, val arrayList:ArrayList<PhotosArray>)
    : FragmentStateAdapter(fragment)  {


    private var fragment: Fragment? = null

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = PopUPServiceDialog()
        val bundle = Bundle()
        // for mp4
       // bundle.putBoolean(PopUPServiceDialog.IS_VIDEO, arrayList.get(position).mediaFile.contains(".mp4"))
        bundle.putString(PopUPServiceDialog.URLIMAGEVIDEO,arrayList.get(position).link)
        bundle.putString(PopUpImageVideoPager.MEDIA_LIST,Gson().toJson(arrayList))

        fragment.arguments = bundle
        return fragment!!

    }



}








