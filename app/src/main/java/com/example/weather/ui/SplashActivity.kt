package com.example.weather.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.weather.R
import com.example.weather.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_splash
        )
    }

    override fun onResume() {

        val animCloud = AnimationUtils.loadAnimation(this,
            R.anim.anim_splash_cloud
        )
        val animSun = AnimationUtils.loadAnimation(this,
            R.anim.anim_splash_sun
        )

        dataBinding.imgSplashCloud.animation = animCloud
        dataBinding.imgSplashSun.animation = animSun

        object : CountDownTimer(3000, 200) {

            override fun onFinish() {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
        super.onResume()
    }
}