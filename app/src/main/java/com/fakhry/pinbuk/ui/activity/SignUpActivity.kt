package com.fakhry.pinbuk.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.pinbuk.BuildConfig
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.model.UserModel
import com.fakhry.pinbuk.utils.Preferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private var googleSignInClient: GoogleSignInClient? = null

    private lateinit var mName: String
    private lateinit var mEmail: String
    private lateinit var mPassword: String

    private lateinit var mFirebaseAuth: FirebaseAuth

    private lateinit var preferences: Preferences

    companion object{
        const val RC_SIGN_IN = 100

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseAuth = FirebaseAuth.getInstance()
        preferences = Preferences(this)

        tv_sign_in.setOnClickListener(this)
        btn_sign_up.setOnClickListener(this)
        btn_sign_up_google.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tv_sign_in -> {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            btn_sign_up -> {
                Log.d("aseloleBiasa", "Aselole 00 : btn email clicked")
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
                        Log.d("aseloleBiasa", "Aselole 01 : btn email clicked")
                        progress_bar.visibility = View.VISIBLE
                        if (checkFormatPassword(mPassword) || checkFormatEmail(mEmail)) {
                            Log.d("aseloleBiasa", "Aselole 02: btn email clicked")
                            doAuthSignUp(mName, mEmail, mPassword)
                        } else {
                            progress_bar.visibility = View.GONE
                        }
                    }
                }
            }
            btn_sign_up_google -> {
                Log.d("aselole", "Aselole 0 : btn clicked")
                doAuthSignUpGoogle()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("aselole", "Aselole 2 : $resultCode")
        Log.d("aselole", "Aselole 2 : $RC_SIGN_IN")

        // Result returned from launching the Intent from GorcogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d("aselole", "Aselole 3 : $task")

            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Log.d("aselole", "Aselole 4 : ${account?.id}")
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("aselole", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        Log.d("aselole", "Aselole 5 : $credential")

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = mFirebaseAuth.currentUser
                Log.d("aselole", "Aselole 6 : ${currentUser?.displayName}")

                if (currentUser != null) {
                    val name = currentUser.displayName
                    val email = currentUser.email
                    val uid = currentUser.uid

                    setDatabase(name!!, email!!, uid)
                    Log.d("aselole", "Aselole 7 : $name")
                    Log.d("aselole", "Aselole 7 : $email")
                    Log.d("aselole", "Aselole 7 : $uid")

                    startActivity(Intent(this, HomeActivity::class.java))
                }
            }
        }
    }

    private fun doAuthSignUpGoogle() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.WEB_CLIENT_AUTH)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        Log.d("aselole", "Aselole 1 : $googleSignInClient")

        val signInIntent = googleSignInClient?.signInIntent
        Log.d("aselole", "Aselole 2 : $RC_SIGN_IN")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun doAuthSignUp(pmName: String, pmEmail: String, pmPassword: String) {
        mFirebaseAuth.createUserWithEmailAndPassword(pmEmail, pmPassword)
            .addOnCompleteListener(this) { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("aseloleBiasa", task.exception?.message.toString())
                } else {
                    Log.d("aseloleBiasa", "Aselole 03: btn email clicked")

                    val uid = FirebaseAuth.getInstance().currentUser!!.uid
                    setDatabase(pmName, pmEmail, uid)
                    progress_bar.visibility = View.GONE

                    val intToHome = Intent(this, HomeActivity::class.java)
                    startActivity(intToHome)
                    finishAffinity()
                }
            }
    }

    private fun setDatabase(pmName: String, pmEmail: String, pmUid: String) {
        val user = UserModel()
        user.user_uid = pmUid
        user.name = pmName
        user.email = pmEmail

        preferences.setValues("name", pmName)
        preferences.setValues("email", pmEmail)
        preferences.setValues("user_uid", pmUid)
        preferences.setValues("status", "1")

        Log.d("aseloleBiasa", "Aselole 04: btn email clicked")

        FirebaseDatabase.getInstance().getReference("users")
            .child(pmUid)
            .setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("aseloleBiasa", "Aselole 05: btn email clicked")
                    Toast.makeText(
                        this, "Registration Success",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this, task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun checkFormatEmail(pmEmail: String): Boolean {
        return if (!(Patterns.EMAIL_ADDRESS.matcher(pmEmail).matches())) {
            et_email.error = "Email doesn't correct"
            et_email.requestFocus()
            false
        } else {
            false
        }
    }

    private fun checkFormatPassword(pmPassword: String): Boolean {
        return if (pmPassword.length < 6) {
            et_password.error = "Password must be at least 6 characters"
            et_password.requestFocus()
            false
        } else {
            true
        }
    }
}
