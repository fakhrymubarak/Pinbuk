package com.fakhry.pinbuk.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_books.*

class BooksActivity : AppCompatActivity(), View.OnClickListener {
    private var mUid: String? = null

    companion object {
        //        internal val TAG = BooksActivity::class.java.simpleName
        const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        mUid = intent.getStringExtra(EXTRA_STATE)
        setBooksFragment()

        btn_back.setOnClickListener (this)
        fab_add.setOnClickListener(this)
    }

    private fun setBooksFragment() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.uid = mUid
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_back -> {
                onBackPressed()
            }
            fab_add -> {
                val intToAdd = Intent(this@BooksActivity, AddNewBooksActivity::class.java)
                intToAdd.putExtra(AddNewBooksActivity.EXTRA_STATE, mUid)
                startActivity(intToAdd)
            }
        }
    }
}