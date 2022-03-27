package com.orwa.buysell.present.main.ad.videoimage.itempager

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.appgallery.R
import com.example.appgallery.databinding.PopupServiceBinding
import com.example.appgallery.util.Util
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


// this is used as the big video
@AndroidEntryPoint
class PopUPServiceDialog : Fragment()/*, Player.EventListener*/ {


    @Inject lateinit var util: Util
    var videoUri: String? = null
    //var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    private var mExoPlayerFullscreen = false
    private var mFullScreenButton: FrameLayout? = null
    private var mFullScreenIcon: ImageView? = null
    private var mFullScreenDialog: Dialog? = null
    private var mResumeWindow: Int = 0
    private var mResumePosition: Long = 0
    var dialogPopUp: Dialog? = null
    lateinit var binding : PopupServiceBinding
   /* override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding= PopUpServiceBinding.inflate(LayoutInflater.from(context))
        dialogPopUp = Dialog(ContextThemeWrapper(activity, R.style.DialogSlideAnim))
        dialogPopUp?.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE)
        dialogPopUp!!.setContentView(binding.root)
        dialogPopUp!!.getWindow()!!.getAttributes().windowAnimations = R.style.DialogSlideAnim

        dialogPopUp!!.window!!.setGravity(Gravity.CENTER)
        dialogPopUp?.setCancelable(false)
        dialogPopUp!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.black)
        exoplayer = (binding.videoFullScreenPlayer) as PlayerView

        //dialogPopUp?.getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogPopUp!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        lp.gravity = Gravity.CENTER
        lp.windowAnimations = R.style.DialogAnimation
        dialogPopUp!!.window!!.attributes = lp
        setvalueBinding()

        //dialogToast?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
// dialogToast!!.getWindow()!!.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialogPopUp!!.show()
        return dialogPopUp!!
    }*/

    private fun setvalueBinding() {
     /*   exoplayer = (binding.videoFullScreenPlayer) as PlayerView
        if (arguments?.getBoolean(IS_VIDEO)==true) {
            setUpVideoInformation()
            initFullscreenDialog()
            initFullscreenButton()
            binding.waterMarkIv.Gone()
        } else  {*/
         //   binding?.videoParent?.visibility = View.GONE
         //   binding.spinnerVideoDetails.Gone()
            Glide.with(binding.imageService.context).load(arguments?.getString(URLIMAGEVIDEO)?:"")
                .error(R.color.gray).placeholder(R.color.gray).dontAnimate()
                .into(binding.imageService)
      //  }

    }
/*
    private fun openFullscreenDialog() {
     (exoplayer.getParent() as ViewGroup).removeView(exoplayer)
        mFullScreenDialog!!.addContentView(
            exoplayer,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

        )

          mFullScreenIcon!!.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_fullscreen_skrink
            )
        )
        mExoPlayerFullscreen = true
        mFullScreenDialog!!.show()


    }

    private fun closeFullscreenDialog() {

       (exoplayer.getParent() as ViewGroup).removeView(exoplayer)
        /*(requireActivity()!!.findViewById(R.id.main_media_frame) as FrameLayout)*/binding.mainMediaFrame.addView(exoplayer)
        mExoPlayerFullscreen = false
        mFullScreenDialog!!.dismiss()
        mFullScreenIcon!!.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext()!!,
                R.drawable.ic_fullscreen_expand
            )
        )
    }

    fun initFullscreenDialog() {

        mFullScreenDialog =
            object : Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                override fun onBackPressed() {
                    if (mExoPlayerFullscreen)
                        closeFullscreenDialog()
                 //   model.dissmissDialog(true) // dismiss please
                    super.onBackPressed()
                }
            }
    }
*/
/*
    fun initFullscreenButton() {
        val controlView = exoplayer.findViewById<PlaybackControlView>(R.id.exo_controller)
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon)
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button)
        mFullScreenButton!!.setOnClickListener {
            if (!mExoPlayerFullscreen)


                openFullscreenDialog()

            else {
                closeFullscreenDialog()
              //  model.dissmissDialog(true)
            }
        }
    }*/

  //  lateinit var rootView: View
  //  lateinit var exoplayer: PlayerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = PopupServiceBinding.inflate(layoutInflater,container,false)
        setvalueBinding()
        binding.downloadConstrain?.setOnClickListener {
            util.downloadImageNew(
                requireContext(), System.currentTimeMillis().toString(),
                arguments?.getString(URLIMAGEVIDEO) ?: ""
            )
        }
       // ifOpenFullScreenDialog()
        return binding.root

    }

   /* private fun setUp() {
        initializePlayer()
        if (videoUri == null) {
            return
        }
        buildMediaSource(Uri.parse(videoUri))
    }

    private fun initializePlayer() {
        if (player == null) { // 1. Create a default TrackSelector
            val loadControl: LoadControl = DefaultLoadControl(
                DefaultAllocator(true, 16),
                MIN_BUFFER_DURATION,
                MAX_BUFFER_DURATION,
                MIN_PLAYBACK_START_BUFFER,
                MIN_PLAYBACK_RESUME_BUFFER,
                -1,
                true
            )
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
            val videoTrackSelectionFactory: TrackSelection.Factory =
                AdaptiveTrackSelection.Factory(bandwidthMeter)
            val trackSelector: TrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            // 2. Create the player
            player = ExoPlayerFactory.newSimpleInstance(requireActivity()!!,
              //  DefaultRenderersFactory(requireActivity()),
                trackSelector,
                loadControl

            )
            binding.videoFullScreenPlayer!!.player = player
        }
    }


    private fun buildMediaSource(mUri: Uri) { // Measures bandwidth during playback. Can be null if not required.
        val bandwidthMeter = DefaultBandwidthMeter()
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(
                requireActivity(),
                Util.getUserAgent(requireActivity(), getString(R.string.app_name)), bandwidthMeter
            )
        // This is the MediaSource representing the media to be played.
        val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mUri)
        // Prepare the player with the source.
        player!!.prepare(videoSource)
        player!!.playWhenReady = true
        player!!.addListener(this)
       // ifOpenFullScreenDialog()
      //  mFullScreenButton!!.performClick()

    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    private fun pausePlayer() {
        if (player != null) {
            player!!.playWhenReady = false
            player!!.playbackState
        }
    }

    private fun resumePlayer() {
        if (player != null) {
            player!!.playWhenReady = true
            player!!.playbackState
        }
    }*/

    override fun onPause() {
        super.onPause()
     /*   if (exoplayer != null && exoplayer.player != null) {
            mResumePosition = Math.max(0, exoplayer.getPlayer()
                    ?.getContentPosition()?:0)
            exoplayer.player?.playWhenReady = false
        }
        pausePlayer()*/
        //  pausePlayer()
        /*    if (mRunnable != null) {
                mHandler!!.removeCallbacks(mRunnable)
            }*/
    }

    override fun onStart() {
        super.onStart()

     //   resumePlayer()
    }
/*
    override fun onDestroy() {
        if (exoplayer != null && exoplayer.getPlayer() != null) {
            mResumeWindow = exoplayer?.getPlayer()?.getCurrentWindowIndex()?:0
            mResumePosition = Math.max(0, exoplayer?.getPlayer()?.getContentPosition()?:0)

            exoplayer?.getPlayer()?.release()
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog!!.dismiss()
        releasePlayer()
        super.onDestroy()
        //super.onDestroy()

    }

    override fun onTimelineChanged(
        timeline: Timeline,
        manifest: Any?,
        reason: Int
    ) {
    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray,
        trackSelections: TrackSelectionArray
    ) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {}
    override fun onPlayerStateChanged(
        playWhenReady: Boolean,
        playbackState: Int
    ) {
        when (playbackState) {
            Player.STATE_BUFFERING -> {
                binding.spinnerVideoDetails!!.visibility = View.VISIBLE
             //   dialogLoader.show()
            }
            Player.STATE_ENDED -> {
            }
            Player.STATE_IDLE -> {
            }
            Player.STATE_READY -> {

                binding.spinnerVideoDetails!!.visibility = View.GONE
             //   dialogLoader.dismiss()

            }
            else -> {
            }
        }
    }*/

    // here we go we get this fragment
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
   //     handleBackPressed()


    }

    override fun onDestroyView() {
     //   removeVideosIfFound()
        super.onDestroyView()
    }

    /*fun removeVideosIfFound() {
        if (exoplayer != null && exoplayer.getPlayer() != null)
            exoplayer.getPlayer()?.release()
        releasePlayer()

    }

    override fun onRepeatModeChanged(repeatMode: Int) {}
    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
    override fun onPlayerError(error: ExoPlaybackException) { //        MyUtil.showSnackErrorInto(this,error.getMessage(),"");
    }

    override fun onPositionDiscontinuity(reason: Int) {}
    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
    override fun onSeekProcessed() {}
    fun setUpVideoInformation() { // this is called when fragment is ready on top
        if (activity !=null) {
            videoUri = getImagePath(arguments?.getString(URLIMAGEVIDEO)?:"") // link
           /* Glide.with(activity!!).load(salon_logo).error(R.color.app_color).centerCrop()
                .into(profile_image_video_Activity!!)*/

            setUp()
        }
    }*/



    companion object {
        //Minimum Video you want to buffer while Playing
        const val MIN_BUFFER_DURATION = 3000
        //Max Video you want to buffer during PlayBack
        const val MAX_BUFFER_DURATION = 5000
        //Min Video you want to buffer before start Playing it
        const val MIN_PLAYBACK_START_BUFFER = 1500
        //Min video You want to buffer when user resumes video
        const val MIN_PLAYBACK_RESUME_BUFFER = 5000
        const val DEFAULT_VIDEO_URL =
            ""
        const val IS_VIDEO = "IS_VIDEO"
        const val URLIMAGEVIDEO ="IMAGE_VIDEO"
    }
}
