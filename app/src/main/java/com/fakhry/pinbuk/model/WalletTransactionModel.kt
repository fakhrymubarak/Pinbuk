package com.fakhry.pinbuk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WalletTransactionModel(
    val is_top_up : Boolean? = false,

    val id_wallet_transaction : String? = "",

    val date : String? = "",
    val detail : String? = "",

    val transaction_amount : Int = 0
) : Parcelable