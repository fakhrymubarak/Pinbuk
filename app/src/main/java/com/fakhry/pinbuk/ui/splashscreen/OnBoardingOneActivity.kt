package com.fakhry.pinbuk.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.ui.signin.SignInActivity
import com.fakhry.pinbuk.utils.Preferences
import kotlinx.android.synthetic.main.activity_onboarding_one.*

class OnBoardingOneActivity : AppCompatActivity() {
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        preferences = Preferences(this)

        if (preferences.getValues("onboarding").equals("1")) {
            finishAffinity()

            val intent = Intent(this@OnBoardingOneActivity,
                SignInActivity::class.java)
            startActivity(intent)
        }


        tv_skip.setOnClickListener{
            finishAffinity()
            val intent = Intent(this@OnBoardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }
        btn_next.setOnClickListener{
            val intent = Intent(this@OnBoardingOneActivity, OnBoardingTwoActivity::class.java )
            startActivity(intent)
        }
    }
}
