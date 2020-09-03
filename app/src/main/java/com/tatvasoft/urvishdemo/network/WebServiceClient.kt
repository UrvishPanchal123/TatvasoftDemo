package com.tatvasoft.urvishdemo.network

import android.annotation.SuppressLint
import android.content.Context
import com.tatvasoft.urvishdemo.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WebServiceClient

/**
 * Private constructor for singleton purpose
 */
private constructor() {

    private var retrofit: Retrofit? = null

    /**
     * The API reference
     */
    private var service: WebApi? = null

    /**
     * @return OkHttpClient with log and custom header interceptors
     */
    private val okHttpClient: OkHttpClient
        get() {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(loggingInterceptor())
            httpClient.readTimeout(READ_TIME_OUT_MIN.toLong(), TimeUnit.MINUTES)
            httpClient.connectTimeout(CONNECT_TIME_OUT_MIN.toLong(), TimeUnit.MINUTES)
            return httpClient.build()
        }

    /**
     *
     * Internal helper and initializer
     *
     */
    private fun initRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        service = retrofit!!.create(WebApi::class.java)
    }

    /**
     * @return Interceptor that provides logging
     */
    private fun loggingInterceptor(): Interceptor {

        val logging = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        return logging
    }

    companion object {

        private const val CONNECT_TIME_OUT_MIN = 5
        private const val READ_TIME_OUT_MIN = 5

        /**
         * Static Object reference
         */
        @SuppressLint("StaticFieldLeak")
        private var webServiceClient: WebServiceClient? = null

        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null

        /**
         * java.lang.String * will init retrofit. needs to be called before using API. preferably from Application class
         *
         * @param context
         */
        fun init(context: Context) {

            if (webServiceClient == null) {
                webServiceClient = WebServiceClient()
                webServiceClient!!.initRetrofit()
                mContext = context
            }
        }

        val retrofitClient: Retrofit?
            get() = webServiceClient!!.retrofit

        /**
         * @return Web API
         */
        fun getService(): WebApi? {
            return webServiceClient!!.service
        }

        val client: Retrofit?
            get() = webServiceClient!!.retrofit
    }
}
