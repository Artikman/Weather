package com.example.weather.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData

class Receiver : BroadcastReceiver() {

    private val internetReceiver: MutableLiveData<String> = MutableLiveData()

    fun getInternetChanged(): MutableLiveData<String> = internetReceiver

    override fun onReceive(context: Context?, intent: Intent?) {
        val actionName = intent?.action
        if(actionName == "android.net.conn.CONNECTIVITY_CHANGE") {
            internetReceiver.postValue("INTERNET")
        } else if(actionName == "android.location.PROVIDERS_CHANGED") {
            internetReceiver.postValue("LOCATION")
        }
    }
}