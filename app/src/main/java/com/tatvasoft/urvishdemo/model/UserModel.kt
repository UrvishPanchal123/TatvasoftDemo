package com.tatvasoft.urvishdemo.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tatvasoft.urvishdemo.R

class UserModel : BaseResponseModel() {

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {

        @SerializedName("users")
        @Expose
        var userList: List<User>? = null

        @SerializedName("has_more")
        @Expose
        var hasMore: Boolean = false
    }

    class User {

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("image")
        @Expose
        var image: String? = null

        @SerializedName("items")
        @Expose
        var items: ArrayList<String>? = null

        companion object {

            @JvmStatic
            @BindingAdapter("bind:imageUrl")
            fun ImageView.loadImage(imagePath: String) {
                if (imagePath.isNotEmpty()) {
                    Glide.with(context)
                        .load(imagePath)
                        .apply(RequestOptions().circleCrop())
                        .error(R.mipmap.ic_launcher_round)
                        .fallback(R.mipmap.ic_launcher_round)
                        .into(this)
                }
            }
        }
    }
}