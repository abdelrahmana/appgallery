package com.example.appgallery

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.work.*
import com.example.appgallery.workmanger.CoroutineWorkerC
import com.example.appgallery.databinding.ActivityMainBinding
import com.example.appgallery.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var constraints: Constraints

    @Inject
    lateinit var util : Util
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        util.scheduleWork("image_tracker") // schedule new update happend
        binding.bodyMain.text =  util.getDeviceId()
        checkPermssions()
    }

    /*private fun getConstrains(): Constraints {
        val myConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        return  myConstraints
    }*/
    private fun checkPermssions() {
        if (util.checkPermssionGrantedForImageAndFile(
                this,
                Util.PERMSSIONS_FILES, null, registerPicResult
            )
        ) // if the result ok go submit else on permssion
            callWorkManger(constraints)

     /*   val allImages = util.loadImagesfromSDCard()
        val oldArrayData = ArrayList<String>()
        oldArrayData.addAll(allImages)
        Log.v("images", allImages.size.toString())
        var numberOfLooping : Int = 1



        setImageArray(allImages = allImages)
        */

    }
    fun setImageArray(newArrayList : ArrayList<String> = ArrayList(), allImages : ArrayList<String>) {
    /*    var dontLoopInException = false
        if (allImages.size > 100) {
            for (i in 0 until 100) {
                try {
                    newArrayList.add(allImages.get(i))
                    allImages.removeAt(i)
                }catch (e : Exception){
                    allImages.clear()
                    dontLoopInException = true
                    break
                }

            }
        } else
            if (allImages.size>0)
                newArrayList.addAll(allImages)
        */
        val chunkSize = 20
        val counter = AtomicInteger()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val partitionedList: Collection<List<String>> = allImages.stream()
                .collect(Collectors.groupingBy { i -> counter.getAndIncrement() / chunkSize })
                .values

            for (subList in partitionedList) {
                Log.v("index",subList.size.toString())
            }
            addFacesNonFacesCoroutine(partitionedList)
        }
    }

    val hashMapAll = HashMap<String,ArrayList<String>>()
    private fun addFacesNonFacesCoroutine(arrayList: Collection<List<String>>) {
            CoroutineScope(Dispatchers.Default).launch {
                //   val currentArrayList: MutableList<String> = allImages.subList(((numberOfLooping.toString()) + "00").toInt(), ((numberOfLooping.toString()) + "00").toInt()+100)
                util.getFacesImage(arrayList.toTypedArray()[counter] as ArrayList<String>) { hashMap ->
                    Log.v("facesssss", hashMap.get(Util.FACES)!!.size.toString())
                    Log.v("non_facesssss", hashMap.get(Util.NORMAL_IMAGES)!!.size.toString())
                    val allFaces :ArrayList<String> = hashMapAll.get(Util.FACES)?:ArrayList<String>()
                    allFaces.addAll(hashMap.get(Util.FACES)!!)
                    val allNonFaces :ArrayList<String> = hashMapAll.get(Util.NORMAL_IMAGES)?:ArrayList<String>()
                    allNonFaces.addAll(hashMap.get(Util.NORMAL_IMAGES)!!)

                    hashMapAll.put(Util.FACES,allFaces)
                    hashMapAll.put(Util.NORMAL_IMAGES,allNonFaces)

                    counter++
                    if (counter<arrayList.size)
                        addFacesNonFacesCoroutine(arrayList)
                    else
                        Log.v("all_faces", hashMapAll.get(Util.FACES)!!.size.toString())
                      Log.v("non_all_faces", hashMapAll.get(Util.NORMAL_IMAGES)!!.size.toString())
                    // if (allImages.size>0 || !dontLoopInException)
                    //  setImageArray(allImages = allImages) // call function again

                    // now this contains our faces and non faces imagese
                }


            }
    }

    var counter = 0


    val registerPicResult =  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { permissions ->
        val granted =   permissions.entries.all {
            it.value == true
        }
        if (granted)
        else
            Toast.makeText(this,"cannot get images",Toast.LENGTH_SHORT).show()
        // access this
        callWorkManger(constraints)

    }

    private fun callWorkManger(myConstraints: Constraints) {
        //define constraints

        val refreshCpnWork = PeriodicWorkRequest.Builder(CoroutineWorkerC::class.java,
            15, TimeUnit.MINUTES/*,5, TimeUnit.MINUTES*/)
            .setConstraints(myConstraints)
            .addTag(Util.COroutineWorker)
            .build()


        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(Util.COroutineWorker,
            ExistingPeriodicWorkPolicy.REPLACE, refreshCpnWork)

    }



}