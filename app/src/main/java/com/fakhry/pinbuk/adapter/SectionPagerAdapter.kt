package com.fakhry.pinbuk.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.ui.fragment.BooksFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var uid: String? = null

    private val tabTables = intArrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
    )

    override fun getItem(position: Int): Fragment {
        return BooksFragment.newInstance(position + 1, uid)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTables[position])
    }

    override fun getCount(): Int = 2
}