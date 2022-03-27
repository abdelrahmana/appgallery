package com.example.appgallery.ui.home.photosuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appgallery.R
import com.example.appgallery.databinding.PhotosImageAdapterBinding
import com.example.appgallery.ui.home.adapter.ClickAction
import com.example.appgallery.ui.home.model.Data
import com.example.appgallery.ui.home.model.PhotosArray

class PhotosUserAdapter(val arrayList : ArrayList<PhotosArray>,
                        val imageClicked : (ClickAction)->Unit
                       // val itemBuy : (Any)->Unit,
) : RecyclerView.Adapter<PhotosUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PhotosImageAdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    //    holder.bindItems(imageModel.image!![position],/*modelData*/imageModel.image!!)
        holder.bindItems(arrayList[position])

        //  setAnimation(holder.itemView, position)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return  arrayList.size
    }
    /*fun updateList(data: List<DataX>) {
        if (arrayListOfImagessValues?.isEmpty()==true) {
            arrayListOfImagessValues?.addAll(data)
            notifyDataSetChanged()
        } else {
            arrayListOfImagessValues?.addAll(data)
            notifyItemRangeInserted(0, arrayListOfImagessValues!!.size - 1)
        }
    }*/
    fun updateList(data: List<PhotosArray>) { // update loader when scroll
        if (arrayList?.isEmpty()==true) {
            arrayList?.addAll(data)
            notifyDataSetChanged()
        } else {
            val prevSize = arrayList.size
            arrayList?.addAll(data)
            // notifyItemRangeRemoved(0, arrayListOfImagessValues!!.size - 1);
            notifyItemRangeChanged(0,data.size)
           // notifyItemRangeInserted(0, arrayList!!.size - prevSize)
        }
    }
    //the class is hodling the list view
    inner class ViewHolder(val imageItemadaptorBinding: PhotosImageAdapterBinding) :
        RecyclerView.ViewHolder(imageItemadaptorBinding.root) {


        fun bindItems(
                itemData: PhotosArray
        ) {

        //    imageItemadaptorBinding.userName.text = itemData.name?:""
            Glide.with(imageItemadaptorBinding.imageUser.context).load(itemData.resizedLink)
                .error(R.color.gray).placeholder(R.color.gray).dontAnimate().into(imageItemadaptorBinding.imageUser)
            // set work status
            //imageItemadaptorBinding.price.text = itemData.?:""

            //  Glide.with(itemView.context).load(itemData).error(R.color.light_gray).dontAnimate().into(itemView.imageItem)
            imageItemadaptorBinding.imageUser.setOnClickListener{
                imageClicked.invoke(ClickAction(arrayList,adapterPosition)) // get image details
            }

        }


    }

//    var defaultSelectedItem = -1 // defqult no thing selected
}