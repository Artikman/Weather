package com.example.weather

import android.app.Application
import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import com.example.weather.utils.Receiver

class App : Application() {

    companion object {
        lateinit var receiver: Receiver
    }

    override fun onCreate() {
        super.onCreate()
        receiver = Receiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
        registerReceiver(
            receiver,
            filter
        )
    }
}