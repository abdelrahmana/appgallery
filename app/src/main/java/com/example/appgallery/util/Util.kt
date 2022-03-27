package com.example.appgallery.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.example.appgallery.R
import com.example.appgallery.util.NameUtil.CAMERA
import com.example.appgallery.util.NameUtil.GALLERY
import com.example.appgallery.workmanger.TrackingGalleryWork
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.seven.util.PrefsModel
import com.seven.util.PrefsUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class Util @Inject constructor(@ApplicationContext val context: Context) {
    @SuppressLint("HardwareIds")
    fun getDeviceId() : String {
   return Settings.Secure.getString(context.getContentResolver(),
            Settings.Secure.ANDROID_ID)
    }
    fun checkAvalibalityOptions(checkEmptyOrNot: Any) :Boolean? { // when check empty strings or int or whatever you need
        if (checkEmptyOrNot is Int) {
            return (checkEmptyOrNot as Int)>0
        }
        else if (checkEmptyOrNot is String)
            return (checkEmptyOrNot as String).isNotEmpty()
        else if (checkEmptyOrNot is java.util.ArrayList<*>)
            return (checkEmptyOrNot).size>0
        return false // no supports value
    }
    fun showSnackMessages(
        activity: Activity?,
        error: String?,color : Int=R.color.red600
    ) {
        if (activity != null) {
            Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                //.title(activity.getString(R.string.errors))
                .message(error!!)
                .backgroundColorRes(color)
                .dismissOnTapOutside()
                .duration(2500)
                .enableSwipeToDismiss()
                .enterAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(550)
                        .alpha()
                        .overshoot()
                )
                .exitAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(200)
                        .anticipateOvershoot()
                )
                .build().show()
        }
    }
    fun changeFragment(targetFragment: Fragment, fragmentManger : FragmentManager, id : Int) { // fragment no back
        fragmentManger
            .beginTransaction()
            .replace(id, targetFragment, "fragment")
            .setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out)
            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

    }
    fun changeFragmentBack(activity: FragmentActivity, fragment: Fragment, tag: String, bundle: Bundle?, id : Int ) {

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (bundle != null) {
            fragment.arguments = bundle
        }
        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
            R.anim.enter_from_left, R.anim.exit_to_right)
        //R.id.frameLayout_direction+
        transaction?.replace(id, fragment, tag)
        transaction?.addToBackStack(tag)
        //    transaction.addToBackStack(null)
        transaction?.commit()

    }
    fun scheduleWork(tag: String) {
        val photoCheckBuilder = OneTimeWorkRequest.Builder(TrackingGalleryWork::class.java)
    /*    val photoCheckBuilder = PeriodicWorkRequest.Builder(TrackingGalleryWork::class.java,
            15, TimeUnit.SECONDS/*,5, TimeUnit.MINUTES*/)*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            photoCheckBuilder.setConstraints(
                Constraints.Builder()
                    .addContentUriTrigger(MediaStore.Images.Media.INTERNAL_CONTENT_URI, true)
                    .addContentUriTrigger(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true)
                    .build()
            )
        }
        val photoCheckWork = photoCheckBuilder.build()
       // val instance = WorkManager.getInstance(context)
        WorkManager.getInstance(context).enqueueUniqueWork(tag,
            ExistingWorkPolicy.REPLACE, photoCheckWork)

    }

    fun getFacesImage(
        arrayList: ArrayList<String>,
        callBackImages: (HashMap<String, ArrayList<String>>) -> Unit
    ) {
        val nonFaceImages = ArrayList<String>()
        val faceImages = ArrayList<String>()

        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(
                FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(
                FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
           // .enableTracking()
            .build()
        // loop arraylist
        for (index in 0 until arrayList.size) {
            try {
                val image = InputImage.fromFilePath(context, File(arrayList.get(index)).toUri())
                val detector = FaceDetection.getClient(options)
                val result = detector.process(image) // detector listener
                    .addOnSuccessListener { faces ->
                        // Task completed successfully
                        // ...
                        faceImages.add(arrayList.get(index))
                        checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                        nonFaceImages.add(arrayList.get(index))

                        checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

                    }


            } catch (e: Exception) {
                nonFaceImages.add(arrayList.get(index))
                checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

            }

        }
      /*  arrayList.forEachIndexed { index, url ->
            try {
              val image = InputImage.fromFilePath(context, File(url).toUri())
                val detector = FaceDetection.getClient(options)
                val result = detector.process(image) // detector listener
                    .addOnSuccessListener { faces ->
                        // Task completed successfully
                        // ...
                        faceImages.add(url)
                        checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                        nonFaceImages.add(url)

                        checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

                    }


            } catch (e: Exception) {
                nonFaceImages.add(url)
                checkLastElement(index,arrayList.size-1,callBackImages,faceImages,nonFaceImages)

            }

        }*/

    }
    private fun checkLastElement(index : Int, lastIndex : Int, callBackImages:
        (HashMap<String, ArrayList<String>>) -> Unit, faceImages : ArrayList<String>,
    nonFaceImages: ArrayList<String>) {
        if (index == lastIndex) // last item now call the call back
        {
            val hashMap =HashMap<String, ArrayList<String>>().also {
                it.put(Util.FACES, faceImages)
                it.put(Util.NORMAL_IMAGES, nonFaceImages)

            }
            callBackImages.invoke(hashMap)
        }
    }
    fun loadImagesfromSDCard(): ArrayList<String> {
        val uri: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor?
        val column_index_data: Int
        val column_index_folder_name: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null

        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
       val orderBy = MediaStore.Images.Media.DATE_TAKEN
        cursor = context.contentResolver.query(uri, projection, null, null, orderBy + " DESC")

        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }
    fun getCreatedFileFromBitmap(fileName: String, bitmapUpdatedImage: Bitmap, typeOfFile : String?, context: Context) : File {
        val bytes =  ByteArrayOutputStream()
        bitmapUpdatedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        /*   val f = new File(Environment.getExternalStorageDirectory()
                   + File.separator + "testimage.jpg");*/
        val f =  initFile(fileName,typeOfFile?:"jpg",context)
        f?.createNewFile()
        val fo =  FileOutputStream(f)
        fo.write(bytes.toByteArray())
        fo.close()

        return f!!
    }
    private fun createImageFileInAppDir(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imagePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return   File(imagePath, "JPEG_${timeStamp}_" + ".jpg")
    }
    fun initFile(name :String,type : String,context: Context): File? {  // to delete file you need to get the absoloute paths for it and it's directory
        var file : File? = null // creating file for video
        file = File( context.cacheDir.absolutePath, SimpleDateFormat(
            "'$name'yyyyMMddHHmmss'.$type'", Locale.ENGLISH).format(Date()))
        //}
        return file
    }
    fun checkPermssionGrantedForImageAndFile(
        context: Activity,
        requestCode: Int,
     //   fragment: Fragment?,
        requestPermssions: ActivityResultLauncher<Array<String>>
    ) : Boolean {
        var allow = false
        // imageView.setOnClickListener {
        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermssions.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }else{
            allow = true

        }

        //  }
        return allow
    }
    fun performImgPicAction(
        which: Int,
        fragment: Fragment?,
        context: Activity,
        onActivityResult: ActivityResultLauncher<Intent>,
        registerGallery: ActivityResultLauncher<Intent>?
    ): Uri? {
        var intent: Intent?
        var photoUri : Uri? =null
        if (which == GALLERY) {  // in case we need to get image from gallery
            intent = Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(NameUtil.WHICHSELECTION, GALLERY)
            registerGallery?.launch(intent)

        } else {  // in case we need camera

          /*  intent = Intent()
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE)
            val file = initFile("face,","jpg",context)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file!!.toUri());
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            intent.putExtra(NameUtil.WHICHSELECTION, CAMERA)*/
           intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile: File? = try {
                createImageFileInAppDir(context)
//                initFile("face,","jpg",context)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }

            photoFile?.also { file ->
                val photoURI: Uri = FileProvider.getUriForFile(
                    context,
                    "com.example.android.provider",
                    file
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                 photoUri = photoURI
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
          //  startActivityForResult(intent, ACTION_REQUEST_CAMERA)
            onActivityResult.launch(intent)


        }
        /*  if (fragment != null)
              fragment.startActivityForResult(Intent.createChooser(intent, context.getString(R.string.selection_option)), which)
          else
              context.startActivityForResult(Intent.createChooser(intent, context.getString(R.string.selection_option)), which)
      */
        return  photoUri
    }
     fun downloadImageNew(context: Context,filename: String, downloadUrlOfImage: String) {
        try {
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val downloadUri = Uri.parse(downloadUrlOfImage)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + filename + ".jpg"
                )
            dm!!.enqueue(request)
            Toast.makeText(context, "Image download started.", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Image download failed.", Toast.LENGTH_SHORT).show()
        }
    }
    fun setRecycleView(recyclerView: RecyclerView?, adaptor: RecyclerView.Adapter<*>,
                       verticalOrHorizontal: Int?, context:Context, gridModel: GridModel?,
                       includeEdge : Boolean) {
        var layoutManger : RecyclerView.LayoutManager? = null
        if (gridModel==null) // normal linear
            layoutManger = LinearLayoutManager(context, verticalOrHorizontal!!,false)
        else
        {
            layoutManger = GridLayoutManager(context, gridModel.numberOfItems)
            if (recyclerView?.itemDecorationCount==0)
                recyclerView?.addItemDecoration(SpacesItemDecoration(gridModel.numberOfItems
                    , gridModel.space, includeEdge))
        }
        recyclerView?.apply {
            setLayoutManager(layoutManger)
            setHasFixedSize(true)
            adapter = adaptor

        }
    }
    fun localSignOut(activity: Activity?,intent : Intent,prefsUtil: PrefsUtil) {
        prefsUtil.removeKey(activity!! , PrefsModel.TOKEN)
        prefsUtil.setLoginModel(activity!!, false)
        prefsUtil.removeKey(activity!! , PrefsModel.userModel)
        activity.startActivity(intent) // go to home please
        activity.finishAffinity()
    }
    companion object {
        val COroutineWorker: String ="myWorkManager"
        val FACES = "FACES"
        val NORMAL_IMAGES = "NORMAL"
        val PERMSSIONS_FILES = 100
    }
}