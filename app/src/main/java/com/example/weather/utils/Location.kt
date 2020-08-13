package com.example.weather.utils

import android.content.Context
import android.location.LocationManager

fun Context.isLocation(): Boolean {
    val lm : LocationManager =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
}