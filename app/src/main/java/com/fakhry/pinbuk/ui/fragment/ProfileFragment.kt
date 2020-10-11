package com.fakhry.pinbuk.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.ui.activity.BooksActivity
import com.fakhry.pinbuk.ui.activity.SettingsActivity
import com.fakhry.pinbuk.ui.activity.SignInActivity
import com.fakhry.pinbuk.utils.Preferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(requireContext().applicationContext)

        val avatarUrl = preferences.getValues("avater_url")
        tv_name.text = preferences.getValues("name")
        tv_email.text = preferences.getValues("email")

        setProfilePicture(avatarUrl)

        tv_books.setOnClickListener(this)
        tv_edit_profile.setOnClickListener(this)
        tv_settings.setOnClickListener(this)
        tv_help.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tv_books -> {
                val uid = preferences.getValues("user_uid")
                val intToBooks = Intent(context, BooksActivity::class.java)
                intToBooks.putExtra(BooksActivity.EXTRA_STATE, uid)
                startActivity(intToBooks)
            }
            tv_edit_profile -> {

            }
            tv_settings -> {
                val intToSettings = Intent(activity, SettingsActivity::class.java)
                startActivity(intToSettings)
            }
            tv_help -> {

        }
            btn_logout -> {
                FirebaseAuth.getInstance().signOut()
                activity?.finishAffinity()

                val intToSignIn = Intent(activity, SignInActivity::class.java)
                startActivity(intToSignIn)

                preferences.clearValues()
            }
        }
    }

    private fun setProfilePicture(pmAvatarUrl: String?) {
        if (pmAvatarUrl == "") {
            iv_profile.setImageResource(R.drawable.ic_profile_grey_24dp)
        } else {
            Glide.with(this)
                .load(pmAvatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)
        }
    }
}