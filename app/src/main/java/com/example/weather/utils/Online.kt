package com.example.weather.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

fun isOnline(context: Context?): Boolean {
    context ?: return false
    val networkInfo: NetworkInfo? =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .activeNetworkInfo

    return networkInfo != null && networkInfo.isConnectedOrConnecting
}