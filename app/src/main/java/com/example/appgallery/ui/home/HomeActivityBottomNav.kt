package com.example.appgallery.ui.home
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.appgallery.R
import androidx.fragment.app.activityViewModels
import com.example.appgallery.base.BaseActivity
import com.example.appgallery.base.BaseViewModel
import com.example.appgallery.databinding.ActivityHomeBinding
import com.example.appgallery.introduction.IntroductionActivity
import com.example.appgallery.ui.home.photosfriend.PhotosFriendsListFragment
import com.example.appgallery.ui.home.photosfriend.PhotosOfMeFragmentList
import com.example.appgallery.util.NameUtil
import com.example.appgallery.util.Util
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.seven.util.PrefsUtil
import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject

@AndroidEntryPoint
class HomeActivityBottomNav : BaseActivity() {
    var binding : ActivityHomeBinding?=null
    var selectedFragment: Fragment? = null
    val model : BaseViewModel by viewModels()
 //   var receiver : CustomReceiver?=null
    @Inject
    lateinit var util: Util
    @Inject lateinit var prefsUtil: PrefsUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Firebase.setAndroidContext(this)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(/*R.layout.activity_home*/binding?.root)
       // receiver = CustomReceiver(model!!)
      //  registerReceiver(receiver, IntentFilter(FirebaseNotification.MY_TRIGGER))
  //    setFireBaseInfoIfLoggedIn()
     //   if (PrefsUtil.isLoggedIn(this))
     //   getFirebaseFcmTokenBeforeStart(model!!)
       // setFcmToken()
        binding?.bottomNav?.setItemIconTintList(null)
     selectedFragment = PhotosOfMeFragmentList()
     util.changeFragment(selectedFragment!!,supportFragmentManager, R.id.fragment_container_navigation) // else it would be registeration
        createObserverViewModel()
        setBottomNavListener()
        binding?.logOut?.setOnClickListener{
            util.localSignOut(this,Intent(this,IntroductionActivity::class.java),prefsUtil)

        }
      //  setAddAdsClick()
    }


    /*   private fun setFcmToken() {
           model?.authListnerLiveData?.observe(this!!, Observer<Any>{ fcmToken->
               if (fcmToken !=null){
                   if (fcmToken is String){
                       // whenever get this update api call
                       postUpdateToken(CommonPresenter.getAuthActivity(applicationContext),postDispossibleObserver(),
                           TokenRequestBody(UtilKotlin.getDeviceId(applicationContext),fcmToken)
                       )
                   }
                   // when result is coming
                   // here we should set every thing related to this details activity
                   //     model?.setObjectData(null)
               }
           })
       }*/

    override fun onResume() {
        super.onResume()

    }
    private fun createObserverViewModel() {
        model?._loadPreviousNavBottom?.observe(this , Observer<Int> { updatedId ->
            if (updatedId !=null) {
                // this is the id
                //bottomNavigationView.
                binding?.bottomNav?.menu?.findItem(updatedId)?.setChecked(true)
                /* if (updatedId == "discover")
                 bottomNavigationView.getMenu().findItem(R.id.discover_nav_id).setChecked(true)
                 else  if (updatedId == "map")
                     bottomNavigationView.getMenu().findItem(R.id.map_nav_id).setChecked(true)
                 else  if (updatedId == "order")
                     bottomNavigationView.getMenu().findItem(R.id.myorder_nav_id).setChecked(true)
                 else  if (updatedId == "saved")
                     bottomNavigationView.getMenu().findItem(R.id.saved_nav_id).setChecked(true)
                 else  if (updatedId == "profile")
                     bottomNavigationView.getMenu().findItem(R.id.profile_nav_id).setChecked(true)*/


                //new update
              //  setCurrentCheckedItemIfAvaliable(updatedId,true) // when observe so we only need the selectdd not all action so send filter
            }

        })
    }
 /*   private fun listenWhenCallBackUserReturn(userStatus: UserStatus){
        // if guest intent will fire
        userStatus.doOperations { callBack :  CallBacksInterface?->
            callBack?.getIntent()?.let {
                startActivity(it)
            }
            // if logged user this would be fired
            callBack?.getFragmentInfo()?.let {fragment->
                selectedFragment = fragment
                checkFragmentAndChange(selectedFragment!!,
                    supportFragmentManager.findFragmentById(R.id.fragment_container_navigation)!!
                )
            }

        }
    }*/
  private fun setBottomNavListener() {
        val navListener =
            BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
     //           previousFragment = selectedFragment // previous to be equal to selected
               // if (isDoubleSelection(menuItem.itemId)){
                /*    val f = supportFragmentManager.findFragmentById(R.id.fragment_container_navigation)
                        if (f == selectedFragment) // do something with f
                            return@OnNavigationItemSelectedListener true*/

               // }
                binding?.bottomNav?.menu?.setGroupCheckable(0, true, true)

                when (menuItem.itemId) {
                    R.id.photosItem -> {
                  //    selectedFragment =  HomeFragment()
                        //selectedFragment = HomeFragment()
                      //  backStack = "home"
                        /*handling user double clicking the bottomnav icon*/
                    /*    if (isDoubleSelection(R.id.homeIconNav)){
                            return@OnNavigationItemSelectedListener true
                        }*/
                       selectedFragment =  PhotosOfMeFragmentList()
                      //  requestCode = 100
                        checkFragmentAndChange(selectedFragment!!,
                            supportFragmentManager.findFragmentById(R.id.fragment_container_navigation)!!
                        )
                    }
                    R.id.friends_photos -> {
                        selectedFragment = PhotosFriendsListFragment()
                        checkFragmentAndChange(selectedFragment!!,
                            supportFragmentManager.findFragmentById(R.id.fragment_container_navigation)!!)
                       /* userStatus.setAction(null,OrderFragmentList(),
                            (Intent(this, ContainerActivityForFragment::class.java)))
                        listenWhenCallBackUserReturn(userStatus)*/
                    //   selectedFragment = SearchFragmnetMap()
                        /*handling user double clicking the bottomnav icon*/
                     /*   if (isDoubleSelection(R.id.doctorIconNav)){
                            return@OnNavigationItemSelectedListener true
                        }*/

                     //   requestCode = 101

                    }
               /*     R.id.notificationUser -> {
                        userStatus.setAction(null,NotificationListFragment(),
                            (Intent(this, ContainerActivityForFragment::class.java)))
                        listenWhenCallBackUserReturn(userStatus)

                        /*   if (PrefsUtil.isLoggedIn(applicationContext)==true)
                               selectedFragment = NotificationListFragment()
                           else {
                               startActivity(Intent(this!!, ContainerActivityForFragment::class.java)) // login
                               true
                           }*/
                    }
                    R.id.profile-> {
                        /*if (PrefsUtil.isLoggedIn(applicationContext))
                            selectedFragment = ProfileFragment()
                        else {
                            startActivity(Intent(this!!, ContainerActivityForFragment::class.java)) // login
                            true
                        }*/
                        userStatus.setAction(null,ProfileFragment(),
                            (Intent(this, ContainerActivityForFragment::class.java)))
                        listenWhenCallBackUserReturn(userStatus)

                    }*/
                }


                true
            }
        //getDataIntent();
        binding?.bottomNav?.setOnNavigationItemSelectedListener(navListener)
  //    if (PrefsUtil.isLoggedIn(this))
   //   addBadges()
    }

    private fun checkFragmentAndChange(selectedFragment : Fragment,currentFragment:Fragment) {
       // val f = supportFragmentManager.findFragmentById(R.id.fragment_container_navigation)
        if (currentFragment!!.javaClass != selectedFragment!!.javaClass) // do something with f

        {

            //       val bundle = Bundle ()
            util.changeFragmentBack(this,
                selectedFragment!!,
                "",
                null,
                R.id.fragment_container_navigation,

                )

        }
    }


    override fun onDestroy() {
       // unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onBackPressed() {
  //  val count = supportFragmentManager.backStackEntryCount
    if ( supportFragmentManager.backStackEntryCount > 0) {
        val f = supportFragmentManager.findFragmentById(R.id.fragment_container_navigation)
   //     else{
            supportFragmentManager.popBackStack()
            //return
     //   }
    } else {
        super.onBackPressed()
    }


}

}
