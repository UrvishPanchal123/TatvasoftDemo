package com.tatvasoft.urvishdemo.network

import com.google.gson.GsonBuilder
import com.tatvasoft.urvishdemo.model.BaseResponseModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class CustomApiCallback<T : BaseResponseModel> : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {

        val data = getResponse(response, BaseResponseModel::class.java as Class<T>)
        val isError = isErrorInWebResponse(response.code())

        if (isError) {

            if (data?.message != null && data.message!!.isNotEmpty()) {
                handleResponseData(response.body())
            } else {
                showErrorMessage("Something went wrong. Please try again")
            }

        } else {
            handleResponseData(response.body())
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        if (isKnownException(t)) {
            showErrorMessage("Due to network connection error we\\'re having trouble.")
        } else {
            showErrorMessage("Something went wrong. Please try again")
        }
    }

    protected abstract fun handleResponseData(data: T?)

    protected abstract fun showErrorMessage(errorMessage: String?)

    private fun isKnownException(t: Throwable): Boolean {
        return (t is ConnectException
                || t is UnknownHostException
                || t is SocketTimeoutException
                || t is IOException)
    }

    private fun isErrorInWebResponse(statusCode: Int): Boolean {
        return when (statusCode) {
            200 -> false
            400 -> true
            401 -> true
            404 -> true
            500 -> true
            else -> true
        }
    }

    private fun <T> getResponse(tResponse: Response<T>, tClass: Class<T>): T? {

        if (tResponse.code() in 200..299) {
            var t = tResponse.body()
            if (t == null) {
                t = GsonBuilder().create().fromJson(createErrorMsgJson(), tClass)
            }
            return t
        } else {
            val errorConverter =
                WebServiceClient.retrofitClient!!.responseBodyConverter<T>(tClass, arrayOfNulls(0))

            return try {
                errorConverter.convert(tResponse.errorBody()!!)
            } catch (e: IOException) {
                e.printStackTrace()
                GsonBuilder().create().fromJson(createErrorMsgJson(), tClass)
            }
        }
    }

    private fun createErrorMsgJson(): String {
        return "\n" +
                "{\n" +
                "  \"Status\": true,\n" +
                "  \"StatusCode\": 0,\n" +
                "  \"Message\": \"Due to network connection error we\\'re having trouble\",\n" +
                "  \"Result\": {\n" +
                "  \n" +
                "  }\n" +
                "}"
    }
}