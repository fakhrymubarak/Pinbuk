package com.fakhry.pinbuk.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.ui.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_two.*

class OnBoardingTwoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_two)

        tv_skip.setOnClickListener{
            finishAffinity()
            val intent = Intent(this@OnBoardingTwoActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        btn_back.setOnClickListener{
            val intent = Intent(this@OnBoardingTwoActivity, OnBoardingOneActivity::class.java )
            startActivity(intent)
        }

        btn_next.setOnClickListener{
            val intent = Intent(this@OnBoardingTwoActivity, OnBoardingThreeActivity::class.java )
            startActivity(intent)
        }
    }
}
