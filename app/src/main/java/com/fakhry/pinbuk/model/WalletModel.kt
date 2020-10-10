package com.fakhry.pinbuk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WalletModel(
    val wallet_id : String? = "",

    val amount : Int = 0,
    val total_transaction : String? = "",

    val list_wallet_transaction : String? = "", //take "id_wallet_transaction" then separate it with comma
) : Parcelable