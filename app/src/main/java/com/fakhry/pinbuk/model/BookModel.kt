package com.fakhry.pinbuk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookModel(
    val isbn : String? = "",

    val title : String? = "",
    val authors : String? = "",

    val category : String? = "",
    val cover_url : String? = "",

    val year_published : Int? = null
) : Parcelable