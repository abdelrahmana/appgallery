package com.example.appgallery.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appgallery.R
import com.example.appgallery.databinding.PhotosRootItemBinding
import com.example.appgallery.ui.home.model.Data
import com.example.appgallery.ui.home.model.PhotosArray
import com.example.appgallery.util.GridModel
import com.example.appgallery.util.Util

class PhotosRootAdapter(val imageClicked : (ClickAction)->Unit,
                        val showMoreClicked : (Data)->Unit
                        , val arrayListOfImagessValues: ArrayList<Data>,
                         val util: Util
                       // val itemBuy : (Any)->Unit,
) : RecyclerView.Adapter<PhotosRootAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PhotosRootItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }
    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    //    holder.bindItems(imageModel.image!![position],/*modelData*/imageModel.image!!)
        holder.bindItems(arrayListOfImagessValues[position])

        //  setAnimation(holder.itemView, position)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return  arrayListOfImagessValues.size
    }
    fun updateList(data: List<Data>) { // update loader when scroll
        if (arrayListOfImagessValues.isEmpty()==true) {
            arrayListOfImagessValues.addAll(data)
            notifyDataSetChanged()
        } else {
            val prevSize = arrayListOfImagessValues.size
            arrayListOfImagessValues.addAll(data)
            notifyItemRangeChanged(0,data.size)

            //   notifyItemRangeInserted(0, arrayListOfImagessValues!!.size - prevSize)
        }
    }

    //the class is hodling the list view
    inner class ViewHolder(val imageItemadaptorBinding: PhotosRootItemBinding) :
        RecyclerView.ViewHolder(imageItemadaptorBinding.root) {


        fun bindItems(
                itemData: Data
        ) {
            imageItemadaptorBinding.userName.text = itemData.taggedUser?:""
        /*    Glide.with(imageItemadaptorBinding.userName.context).load(itemData.resizedLink)
                .error(R.color.gray).placeholder(R.color.gray).dontAnimate().into(imageItemadaptorBinding.imageProfile)
        */
            // set work status
            //imageItemadaptorBinding.price.text = itemData.?:""

            //  Glide.with(itemView.context).load(itemData).error(R.color.light_gray).dontAnimate().into(itemView.imageItem)
            imageItemadaptorBinding.imageProfile.setOnClickListener{
                showMoreClicked.invoke((itemData))
            }
            val adapter = PhotosImageAdapter(imageClicked,itemData,itemData.photosCount,showMoreClicked)
            util.setRecycleView(imageItemadaptorBinding.imageGridRecycle,adapter,
                null,imageItemadaptorBinding.imageProfile.context,GridModel(2,15),false)
        }


    }

//    var defaultSelectedItem = -1 // defqult no thing selected
}
//class ClickAction(item : Datax)