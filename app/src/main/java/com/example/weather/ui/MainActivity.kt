package com.example.weather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather.App
import com.example.weather.R
import com.example.weather.utils.isOnline
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)

        App.receiver.getInternetChanged().observe(this, Observer {
            Log.d("QWERTY", it)
            if (it == "INTERNET") {
                getInternetMessage(isOnline(this))
            }
        })
        }

    private fun getInternetMessage(isOnline: Boolean): Unit =
        if (isOnline) {
            status.text = "Вкл"
            status.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {
            status.text = "Выкл"
            status.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
}