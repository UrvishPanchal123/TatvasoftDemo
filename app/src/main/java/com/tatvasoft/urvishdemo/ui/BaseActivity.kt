package com.tatvasoft.urvishdemo.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tatvasoft.urvishdemo.R

open class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var mBinding: T

    private lateinit var progressDialog: Dialog

    protected fun bindView(layoutId: Int) {
        mBinding = DataBindingUtil.setContentView(this, layoutId)
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    protected fun showLoader(context: Context) {
        progressDialog = Dialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(R.layout.layout_custom_loader)
        progressDialog.window?.setDimAmount(0.75f)
        progressDialog.show()
    }

    protected fun dismissLoader() {
        if (::progressDialog.isInitialized)
            progressDialog.dismiss()
    }

    /**
     * Checking Internet is available or not
     *
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    fun showNoData(
        recyclerView: RecyclerView,
        noDataView: View,
        isNullOrEmpty: Boolean,
        msg: String?
    ) {
        if (isNullOrEmpty) {
            recyclerView.visibility = View.GONE
            noDataView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            noDataView.visibility = View.GONE
        }
    }
}