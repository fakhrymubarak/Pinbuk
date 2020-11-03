package com.fakhry.pinbuk.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.model.UserModel
import com.fakhry.pinbuk.utils.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mEmail: String
    private lateinit var mPassword: String

    private lateinit var mFirebaseDatabase: DatabaseReference

    private lateinit var mFirebaseAuth: FirebaseAuth
    private var mAuthStateListener: AuthStateListener? = null
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("users")
        mFirebaseAuth = FirebaseAuth.getInstance()

        preferences = Preferences(this)

        if (preferences.getValues("status").equals("1")) {
            finishAffinity()
            val intToHome = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(intToHome)
        }

        tv_sign_up.setOnClickListener(this)
        btn_sign_in.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tv_sign_up -> {
                val intToSignUp = Intent(this@SignInActivity, SignUpActivity::class.java)
                startActivity(intToSignUp)
            }
            btn_sign_in -> {
                mEmail = et_email.text.toString().trim()
                mPassword = et_password.text.toString().trim()
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
                        doAuthSignIn(mEmail, mPassword)
                    }
                }
            }
        }
    }

    private fun doAuthSignIn(pmEmail: String, pmPassword: String) {
        progress_bar.visibility = View.VISIBLE
        mAuthStateListener = AuthStateListener {
            val mFirebaseUser = mFirebaseAuth.currentUser
            if (mFirebaseUser != null) {
                progress_bar.visibility = View.GONE
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                progress_bar.visibility = View.GONE
                Toast.makeText(this, "Please SignUp", Toast.LENGTH_SHORT).show()
            }
        }

        mFirebaseAuth.signInWithEmailAndPassword(pmEmail, pmPassword)
            .addOnCompleteListener(this) { task ->
                if (!task.isSuccessful) {
                    Log.d("aseloleBiasa", "Aselole 01 :")
                    tv_invalid.visibility = View.VISIBLE
                    progress_bar.visibility = View.GONE
                } else {
                    val uid = FirebaseAuth.getInstance().currentUser!!.uid
                    getDatabase(uid)
                }
            }
    }

    private fun getDatabase(pmUid: String) {
        mFirebaseDatabase.child(pmUid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userInDatabase = dataSnapshot.getValue(UserModel::class.java)
                if (userInDatabase == null) {
                    Toast.makeText(
                        this@SignInActivity,
                        "Database Error,\nPlease contact our CS.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    preferences.setValues("user_uid", userInDatabase.user_uid.toString())
                    preferences.setValues("name", userInDatabase.name.toString())
                    preferences.setValues("avatar_url", userInDatabase.avatar_url.toString())
                    preferences.setValues("email", userInDatabase.email.toString())
                    preferences.setValues("whats_app_url", userInDatabase.whats_app_url.toString())
                    preferences.setValues("domicile", userInDatabase.domicile.toString())
                    preferences.setValues("list_book_own", userInDatabase.list_book_own.toString())
                    preferences.setValues(
                        "list_chart_books",
                        userInDatabase.list_chart_books.toString()
                    )
                    preferences.setValues(
                        "list_borrowed_books",
                        userInDatabase.list_borrowed_books.toString()
                    )

                    preferences.setValues("wallet_id", userInDatabase.wallet_id.toString())

                    preferences.setValues("list_following", userInDatabase.list_following.toString())
                    preferences.setValues(
                        "following_number",
                        userInDatabase.following_number.toString()
                    )
                    preferences.setValues("list_followers", userInDatabase.list_followers.toString())
                    preferences.setValues(
                        "followers_number",
                        userInDatabase.followers_number.toString()
                    )
                    preferences.setValues("status", "1")

                    progress_bar.visibility = View.GONE
                    tv_invalid.visibility = View.GONE
                    val intToHome = Intent(this@SignInActivity, HomeActivity::class.java)
                    startActivity(intToHome)
                    finishAffinity()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

//    fun getCurrentDate(): String = SimpleDateFormat("yyyy/MM/dd/HH:mm:ss", Locale.getDefault()).format(Date())
}
