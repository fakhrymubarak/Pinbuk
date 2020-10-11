package com.fakhry.pinbuk.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fakhry.pinbuk.model.BookModel
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BooksBorrowingViewModel : ViewModel() {
    private lateinit var mFirebaseDatabaseUsers: DatabaseReference
    private lateinit var mFirebaseDatabaseBooks: DatabaseReference
    private val mListBooksBorrowing = MutableLiveData<ArrayList<BookModel>>()

    companion object {
        val TAG = BooksBorrowingViewModel::class.simpleName
    }

    fun takeListBooksBorrowingFromDatabase(pmUid: String) {
//        Log.d(TAG, "000 ISBN true = $pmUid")
        mFirebaseDatabaseUsers = FirebaseDatabase.getInstance().getReference("users")
        mFirebaseDatabaseBooks = FirebaseDatabase.getInstance().getReference("book")

        mFirebaseDatabaseUsers.child(pmUid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {

                    var listData: ArrayList<BookModel>? = ArrayList()
//                    Log.d(TAG, "1 ISBN true = ${userSnapshot.hasChild("list_books_borrowing")}")
                    if (userSnapshot.hasChild("list_books_borrowing")) {

                        val listBooksOwned = userSnapshot.child("list_books_borrowing")

                        //get list_borrowed_books key and value
                        for (isbnDataSnapshot in listBooksOwned.children) {
                            val isbnToString = isbnDataSnapshot.key
//                            Log.d(TAG, "2 ISBN = $isbnToString")
                            //if key == true
                            if (listBooksOwned.child(isbnToString!!).value!! == true) {
//                                Log.d(TAG, "3 ISBN = $isbnToString")

                                //get books data from database
                                mFirebaseDatabaseBooks.child(isbnToString)
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(bookSnapshot: DataSnapshot) {
                                            val bookFromDatabase = bookSnapshot.getValue(BookModel::class.java)
                                            listData?.add(bookFromDatabase!!)
                                        }
                                        override fun onCancelled(error: DatabaseError) {
                                            Log.e(TAG, error.message)
                                        }
                                    })
                            }
                        }

                    } else {
                        listData = null
                    }
                    GlobalScope.launch (Dispatchers.Main){
                        delay(1000)
                        mListBooksBorrowing.postValue(listData)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, error.message)
                }
            })
    }

    fun getBooks(): LiveData<ArrayList<BookModel>> = mListBooksBorrowing
}