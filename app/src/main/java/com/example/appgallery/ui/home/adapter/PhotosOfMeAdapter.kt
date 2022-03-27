package com.example.appgallery.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appgallery.R
import com.example.appgallery.databinding.PhotosImageAdapterBinding
import com.example.appgallery.databinding.PhotosRootItemBinding
import com.example.appgallery.ui.home.model.Data
import com.example.appgallery.ui.home.model.PhotosArray

class PhotosOfMeAdapter(val imageClickedLUnit : (ClickAction)->Unit
                        , val dataObject : Data,
                        val photosCount : Int,
                        val itemClicked : (Data)->Unit
                       // val itemBuy : (Any)->Unit,
) : RecyclerView.Adapter<PhotosOfMeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PhotosImageAdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    //    holder.bindItems(imageModel.image!![position],/*modelData*/imageModel.image!!)
        holder.bindItems(dataObject.photosArray[position])

        //  setAnimation(holder.itemView, position)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return  dataObject.photosArray.size
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

            if (adapterPosition == dataObject.photosArray.size-1 && dataObject.photosArray.size==4
                && dataObject.photosCount>0) // last item lets show this more
            {
                imageItemadaptorBinding.countedImage.visibility = View.VISIBLE
                imageItemadaptorBinding.countedNumber.visibility = View.VISIBLE
                imageItemadaptorBinding.countedNumber.text = dataObject.photosCount.toString()
            }
            //  Glide.with(itemView.context).load(itemData).error(R.color.light_gray).dontAnimate().into(itemView.imageItem)
            imageItemadaptorBinding.imageUser.setOnClickListener{
                imageClickedLUnit.invoke(ClickAction(dataObject.photosArray as ArrayList<PhotosArray>,adapterPosition)) // get image details
            }
            imageItemadaptorBinding.countedNumber.setOnClickListener{ // get all images related to this user
                itemClicked.invoke((dataObject))
            }

        }


    }

//    var defaultSelectedItem = -1 // defqult no thing selected
}
class ClickAction(val item : ArrayList<PhotosArray>,val selectedPosition : Int)