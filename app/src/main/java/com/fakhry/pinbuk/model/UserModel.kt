package com.fakhry.pinbuk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
//    var id_user: Int = 0,
    var id_user: String? = "",

    var name : String? = "",
    var password : String? = "",
    var avatar_url : String? = "",
    var email : String? = "",
    var joined_at : String? = "",
    var whats_app_url : String? = "",

    var domicile : String? = "",

    var list_book_own : String? = "", //take "usermname_isbn", then separate it with comma

    var list_chart_books : String? = "", //take "username{owner}_isbn", then separate it with comma

    var list_borrowed_books : String? = "", //take "id_transaction", take isbn part, then separate it with comma

    val wallet_id : String? = "",

    val list_following : String? = "", //take username than separate it with comma
    val following_number : String? = "", //take username than separate it with comma

    val list_followers : String? = "", //take username than separate it with comma
    val followers_number : String? = "" //take username than separate it with comma

) : Parcelable