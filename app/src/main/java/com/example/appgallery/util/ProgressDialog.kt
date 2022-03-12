package com.example.appgallery.util

import android.app.Dialog

interface ProgressDialog {
    fun setDialog(progressDialog : Dialog)
 //   fun setShimmerLoader(shimmerRecycle: ShimmerRecyclerView)

}
class ShowProgress():ProgressDialog {
    override fun setDialog(progressDialog: Dialog) {
        progressDialog.show()
    }

  /*  override fun setShimmerLoader(shimmerRecycle: ShimmerRecyclerView) {
        shimmerRecycle.showShimmer()
    }*/
}
class DissMissProgress():ProgressDialog {
    override fun setDialog(progressDialog: Dialog) {
        progressDialog.dismiss()
    }

 /*   override fun setShimmerLoader(shimmerRecycle: ShimmerRecyclerView) {
        shimmerRecycle.hideShimmer()

    }*/
}