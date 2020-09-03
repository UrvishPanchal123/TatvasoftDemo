package com.tatvasoft.urvishdemo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tatvasoft.urvishdemo.model.UserModel
import com.tatvasoft.urvishdemo.network.CustomApiCallback
import com.tatvasoft.urvishdemo.network.WebServiceClient

class UserListViewModel : ViewModel() {

    fun getUserList(offset: Int, limit: Int): MutableLiveData<UserModel> {

        val userResponseModel = MutableLiveData<UserModel>()

        WebServiceClient.getService()?.getUserList(offset, limit)
            ?.enqueue(object : CustomApiCallback<UserModel>() {

                override fun handleResponseData(data: UserModel?) {
                    userResponseModel.value = data
                }

                override fun showErrorMessage(errorMessage: String?) {
                    Log.e(UserListViewModel::javaClass.name, errorMessage.toString())
                    userResponseModel.value = null
                }
            })

        return userResponseModel
    }
}