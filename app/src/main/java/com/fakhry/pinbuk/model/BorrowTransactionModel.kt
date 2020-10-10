package com.fakhry.pinbuk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BorrowTransactionModel(
    val id_borrow_transaction : String? = "",

    val id_user_owner : Int? = 0,
    val id_user_borrower : Int? = 0,

    val isbn : String? = "",

    val date_borrowing : String? = "",
    val date_due : String? = "",

    val price_borrow_per_day : Int = 0,
    val fine : Int = 0,

    val total_price : Int = 0
) : Parcelable