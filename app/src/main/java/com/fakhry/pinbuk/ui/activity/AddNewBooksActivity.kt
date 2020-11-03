package com.fakhry.pinbuk.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.model.BookModel
import kotlinx.android.synthetic.main.activity_add_new_books.*


class AddNewBooksActivity : AppCompatActivity(), View.OnClickListener {

    private var mUid: String? = null

    private lateinit var mFirebaseDatabaseUsers: DatabaseReference
    private lateinit var mFirebaseDatabaseBooks: DatabaseReference

    companion object {
        val TAG: String? = AddNewBooksActivity::class.java.simpleName
        const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_books)

        mUid = intent.getStringExtra(BooksActivity.EXTRA_STATE)

        mFirebaseDatabaseUsers = FirebaseDatabase.getInstance().getReference("users")
        mFirebaseDatabaseBooks = FirebaseDatabase.getInstance().getReference("book")


        btn_back.setOnClickListener(this)
        btn_add_new_book.setOnClickListener(this)

        et_isbn.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                checkBookUserIsbn(et_isbn.text.toString().trim())
            }
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            btn_back -> {
                onBackPressed()
            }
            btn_add_new_book -> {

            }
        }
    }

    private fun checkBookUserIsbn(isbnToCheck: String) {
        mFirebaseDatabaseUsers.child(mUid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {

                    if (userSnapshot.hasChild("list_books_owned")) {

                        val listBooksOwned = userSnapshot.child("list_books_owned")

                        //get list_borrowed_books key and value
                        for (isbnDataSnapshot in listBooksOwned.children) {
                            val isbnToString = isbnDataSnapshot.key
                            when {
                                listBooksOwned.child(isbnToString!!).value!! == true -> {
                                    et_isbn.error = "You've already have this book"
                                }
                                listBooksOwned.child(isbnToString).value!! == false -> {
                                    et_isbn.error = "Books already set to available"
                                    mFirebaseDatabaseUsers.child(mUid!!).child("list_books_owned")
                                        .child(isbnToString).setValue(true)
                                }
                                else -> {
                                    mFirebaseDatabaseUsers.child(mUid!!).child("list_books_owned").child(isbnToCheck).setValue(true)
                                    checkBookDatabaseIsbn(isbnToCheck)
                                }
                            }
                        }
                    }else{
                        mFirebaseDatabaseUsers.child(mUid!!).child("list_books_owned").child(isbnToCheck).setValue(true)
                        checkBookDatabaseIsbn(isbnToCheck)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, error.message)
                }
            })
    }

    private fun checkBookDatabaseIsbn(isbnToCheck: String) {
        mFirebaseDatabaseBooks.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(isbnToCheck)){
                    //seToDatabase
                }else{
                    //doNothing, but after upgrade user database, use here
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, error.message)
            }

        })

    }


    private fun setBookDatabase(pmIsbn : String, pmTitle : String, pmAuthor : String, pmYearPublished : String, pmCoverUrl: String) {
        val book = BookModel(pmIsbn, pmTitle, pmAuthor, pmYearPublished, pmCoverUrl)
        FirebaseDatabase.getInstance().getReference("users")
            .child(pmIsbn)
            .setValue(book).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Set Book to database success")
                } else {
                    Log.e(TAG, task.exception?.message!!)
                }
            }
    }

}