package com.fakhry.pinbuk.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.pinbuk.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //put preference checker here

        GlobalScope.launch {
            delay(3000L)
            val intent = Intent(
                this@SplashScreenActivity,
                OnBoardingOneActivity::class.java
            )
            startActivity(intent)
            finish()
        }
    }
}