package com.tatvasoft.urvishdemo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseResponseModel {

    @SerializedName("status")
    @Expose
    var status: Boolean = false

    @SerializedName("message")
    @Expose
    var message: String? = null
}