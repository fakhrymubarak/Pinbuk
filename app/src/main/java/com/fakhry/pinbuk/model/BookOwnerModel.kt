package com.fakhry.pinbuk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookOwnerModel(
    val id_user_book_owner: String? = "",

    val id_user_owner: Int? = 0,
    val isbn: String? = "",

    val price_borrow_per_day: Int? = 0,
    val stock: Int? = 0,

    val is_available: Boolean? = true
) : Parcelable