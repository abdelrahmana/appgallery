package com.orwa.buysell.present.main.ad.videoimage

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import com.example.appgallery.R
import com.example.appgallery.databinding.ImageSliderViewpagerBinding
import com.example.appgallery.util.GetObjectGson
import com.orwa.buysell.present.main.ad.videoimage.adapter.ViewPagerAdapterImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//this class make error and animation style
@AndroidEntryPoint
class PopUpImageVideoPager : DialogFragment() {
    var dialogBidding: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @Inject lateinit var gsonObject : GetObjectGson

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //createObserverViewModel()
        //setGlobalScope()
  //      initViews(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    lateinit var binding : ImageSliderViewpagerBinding

 /*   override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ImageSliderViewpagerBinding.inflate(inflater,container,false)
        return binding.root
    }*/

    // this is really called when you call show and this is called when we call show on activity it self
   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBidding = Dialog(ContextThemeWrapper(activity, R.style.DialogSlideAnim))
        dialogBidding?.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE)
        binding= ImageSliderViewpagerBinding.inflate(LayoutInflater.from(context))

        dialogBidding!!.setContentView(binding.root)

        dialogBidding!!.getWindow()!!.getAttributes().windowAnimations = R.style.DialogSlideAnim
        if (dialogBidding!!.isShowing)
            dialogBidding!!.dismiss()
        dialogBidding!!.show()
        dialogBidding!!.window!!.setGravity(Gravity.CENTER)
        dialogBidding?.setCancelable(false)
        dialogBidding!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialogBidding?.getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogBidding!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        lp.gravity = Gravity.CENTER
        lp.windowAnimations = R.style.DialogAnimation
        dialogBidding!!.window!!.attributes = lp

        //dialogToast?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
// dialogToast!!.getWindow()!!.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialogBidding!!.show()
        initViews(dialogBidding,arguments?.getInt(SELECTEDIMAGE)?:0)
        return dialogBidding!!
    }

    private fun initViews(binder: Dialog?, position: Int) {
       val viewPagerAdaptor =
            ViewPagerAdapterImage(
                this@PopUpImageVideoPager,
                gsonObject.getPhotoArray(arguments?.getString(MEDIA_LIST)?:"")?:ArrayList()
            )
        binding.sliderViewPager.doOnPreDraw {
            binding.sliderViewPager.currentItem = position
        }
        binding.sliderViewPager.adapter = viewPagerAdaptor
     //   binding.sliderViewPager.currentItem = position

    }

    companion object {
        val MEDIA_LIST = "MEDIA"
        val SELECTEDIMAGE = "SELECTED_IMAGE"
    }
}
class ReportModel (var reason :String)
