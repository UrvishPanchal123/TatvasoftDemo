package com.tatvasoft.urvishdemo

import android.app.Application
import com.tatvasoft.urvishdemo.network.WebServiceClient

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize API client
        WebServiceClient.init(this)
    }
}