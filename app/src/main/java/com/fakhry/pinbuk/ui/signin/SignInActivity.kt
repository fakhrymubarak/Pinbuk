package com.fakhry.pinbuk.ui.signin

import android.content.Intent
import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.pinbuk.HomeActivity
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.ui.settings.SettingsActivity
import com.fakhry.pinbuk.ui.signup.SignUpActivity
import com.fakhry.pinbuk.utils.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.text.SimpleDateFormat
import java.util.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mEmail: String
    private lateinit var mPassword: String

    private lateinit var mDatabase: DatabaseReference

    private lateinit var mFirebaseAuth: FirebaseAuth
    private var mAuthStateListener: AuthStateListener? = null
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        mFirebaseAuth = FirebaseAuth.getInstance()
        preferences = Preferences(this)

        if (preferences.getValues("status").equals("1")) {
            finishAffinity()
            val intent = Intent(
                this@SignInActivity,
                HomeActivity::class.java
            )
            startActivity(intent)
        }

        btn_sign_in.setOnClickListener(this)
        tv_sign_up.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tv_sign_up -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            btn_sign_in -> {
                mEmail = et_email.text.toString()
                mPassword = et_password.text.toString()

                when {
                    mEmail == "" -> {
                        et_email.error = "Email must be filled"
                        et_password.requestFocus()
                    }
                    mPassword == "" -> {
                        et_password.error = "Password must be filled"
                        et_password.requestFocus()
                    }
                    else -> {
                        pushLogin(mEmail, mPassword) //METHOD UNTUK LOGIN
                    }
                }
            }
            tv_sign_up -> {
                val intent = Intent(
                    this@SignInActivity,
                    SignUpActivity::class.java
                )
                startActivity(intent)
            }
        }
    }

    private fun pushLogin(pmEmail: String, pmPassword: String) {
        mAuthStateListener = AuthStateListener {
            val mFirebaseUser = mFirebaseAuth.currentUser
            if (mFirebaseUser != null) {
                Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show()
            }
        }

        when {
            pmEmail.isEmpty() -> {
                et_email.error = "Please enter your email"
                et_email.requestFocus()
            }
            pmPassword.isEmpty() -> {
                et_password.error = "Please enter your password"
                et_password.requestFocus()
            }
            !(pmEmail.isEmpty() && pmPassword.isEmpty()) -> {
                mFirebaseAuth.signInWithEmailAndPassword(pmEmail, pmPassword)
                    .addOnCompleteListener(this) { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Login Error, Please Login Again",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            preferences.setValues("status", "1")
                            finishAffinity()
                            val intToHome = Intent(this, SettingsActivity::class.java)
                            startActivity(intToHome)
                        }
                    }
            }
            else -> {
                Toast.makeText(this, "Error Occurred!", Toast.LENGTH_SHORT).show()
            }
        }

    }
//
//    private fun getUserData(pmEmail: String, pmPassword: String) {
//        val user =
//
//        preferences.setValues("id_user", user.id_user.toString())
//        preferences.setValues("name", user.name.toString())
//        preferences.setValues("username", user.username.toString())
//        preferences.setValues("password", user.password.toString())
//        preferences.setValues("avatar_url", user.avatar_url.toString())
//        preferences.setValues("email", user.email.toString())
//        preferences.setValues("joined_at", user.joined_at.toString())
//        preferences.setValues("whats_app_url", user.whats_app_url.toString())
//
//        preferences.setValues("domicile", user.domicile.toString())
//        preferences.setValues("list_book_own", user.list_book_own.toString())
//        preferences.setValues(
//            "list_chart_books",
//            user.list_chart_books.toString()
//        )
//        preferences.setValues(
//            "list_borrowed_books",
//            user.list_borrowed_books.toString()
//        )
//
//        preferences.setValues("wallet_id", user.wallet_id.toString())
//
//        preferences.setValues("list_following", user.list_following.toString())
//        preferences.setValues(
//            "following_number",
//            user.following_number.toString()
//        )
//        preferences.setValues("list_followers", user.list_followers.toString())
//        preferences.setValues(
//            "followers_number",
//            user.followers_number.toString()
//        )
//
//        preferences.setValues("status", "1") //SET STATUS PREFERENCES
//
//        val name = preferences.getValues("name")
//        Toast.makeText(
//            this@SignInActivity,
//            "Selamat Datang '$name'",
//            Toast.LENGTH_LONG
//        ).show()
//
//    }

//    private fun pushLogin(iUsername: String, iPassword: String) {
//        mDatabase.child(iUsername).addValueEventListener(
//            object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    val user = dataSnapshot.getValue(UserModel::class.java)
//
//                    if (user == null) {
//                        Toast.makeText(
//                            this@SignInActivity,
//                            "Email wasn't register",
//                            Toast.LENGTH_LONG
//                        )
//                            .show()
//
//                    } else {
//                        if (user.password.equals(iPassword)) {
//
//
//                            val intent = Intent(
//                                this@SignInActivity,
//                                HomeActivity::class.java
//                            )
//                            startActivity(intent)
//
//                        } else {
//                            Toast.makeText(
//                                this@SignInActivity,
//                                "Incorrect email or password",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//    }

    fun getCurrentDate(): String =
        SimpleDateFormat("yyyy/MM/dd/HH:mm:ss", Locale.getDefault()).format(Date())
}
