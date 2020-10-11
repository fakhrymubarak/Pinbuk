package com.fakhry.pinbuk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookModel(
    var isbn : String? = "",

    var title : String? = "",
    var author : String? = "",

    var category : String? = "",
    var cover_url : String? = "",

    var year_published : String? = ""
) : Parcelable