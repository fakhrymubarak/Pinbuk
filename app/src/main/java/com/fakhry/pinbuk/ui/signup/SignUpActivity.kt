package com.fakhry.pinbuk.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.model.UserModel
import com.fakhry.pinbuk.ui.settings.SettingsActivity
import com.fakhry.pinbuk.ui.signin.SignInActivity
import com.fakhry.pinbuk.utils.Preferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.et_email
import kotlinx.android.synthetic.main.activity_sign_up.et_password


class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mName: String
    private lateinit var mEmail: String
    private lateinit var mPassword: String

    private lateinit var mFirebaseAuth: FirebaseAuth

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseAuth = FirebaseAuth.getInstance()
        preferences = Preferences(this)
        btn_sign_up.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            tv_sign_in -> {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            btn_sign_up -> {
                mName = et_name.text.toString().trim()
                mEmail = et_email.text.toString().trim()
                mPassword = et_password.text.toString().trim()

                when {
                    mName == "" -> {
                        et_email.error = "Name must be filled"
                        et_password.requestFocus()
                    }
                    mEmail == "" -> {
                        et_email.error = "Email must be filled"
                        et_password.requestFocus()
                    }
                    mPassword == "" -> {
                        et_password.error = "Password must be filled"
                        et_password.requestFocus()
                    }
                    else -> {
                        if (checkPassword(mPassword) || checkEmail(mEmail)) {
                            pushSignUp(mName, mEmail, mPassword)
                        }
                    }
                }
            }
        }
    }

    private fun checkEmail(pmEmail: String): Boolean {
        return if (Patterns.EMAIL_ADDRESS.matcher(pmEmail).matches()) {
            true
        } else {
            Toast.makeText(this, "Email doesn't correct", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun checkPassword(pmPassword: String): Boolean {
        return if (pmPassword.length < 6) {
            et_password.error = "Password must be at least 6 characters"
            false
        } else {
            true
        }
    }

    private fun pushSignUp(
        pmName: String,
        pmEmail: String,
        pmPassword: String
    ) {
        mFirebaseAuth.createUserWithEmailAndPassword(pmEmail, pmPassword)
            .addOnCompleteListener(
                this
            ) { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    inputToDatabase(pmName, pmEmail)
                    val intToHome = Intent(this, SettingsActivity::class.java)
                    startActivity(intToHome)
                    finishAffinity()
                }
            }
    }

    private fun inputToDatabase(pmName: String, pmEmail: String) {
        val user = UserModel()

        user.name = pmName
        user.email = pmEmail

        preferences.setValues("name", pmName)
        preferences.setValues("email", pmEmail)

        preferences.setValues("status", "1")

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().getReference("users")
            .child(uid)
            .setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this, "Registration Sucsessfully",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    tv_invalid.visibility = View.VISIBLE
                    Toast.makeText(
                        this, "Failed to login, Please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}