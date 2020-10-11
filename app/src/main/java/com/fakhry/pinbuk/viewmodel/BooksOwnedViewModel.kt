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

class BooksOwnedViewModel : ViewModel() {
    private lateinit var mFirebaseDatabaseUsers: DatabaseReference
    private lateinit var mFirebaseDatabaseBooks: DatabaseReference

    private val mListBooksOwned = MutableLiveData<ArrayList<BookModel>>()

    companion object {
        val TAG = BooksOwnedViewModel::class.simpleName
    }

    fun takeListBooksOwnedFromDatabase(pmUid: String) {
        Log.d(TAG, "0 UID = $pmUid")
        mFirebaseDatabaseUsers = FirebaseDatabase.getInstance().getReference("users")
        mFirebaseDatabaseBooks = FirebaseDatabase.getInstance().getReference("book")
        mFirebaseDatabaseUsers.child(pmUid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {

                    val listData: ArrayList<BookModel>? = ArrayList()
//                    Log.d(TAG,"1 list_book_owned true = ${userSnapshot.hasChild("list_books_owned")}")
                    if (userSnapshot.hasChild("list_books_owned")) {
                        val lvListBooksOwned = userSnapshot.child("list_books_owned")
                        //get list_borrowed_books key and value
                        for (isbnDataSnapshot in lvListBooksOwned.children) {
                            val isbnToString = isbnDataSnapshot.key
//                            Log.d(TAG, "2 ISBN to String= $isbnToString")
                            //if key == true
                            if (lvListBooksOwned.child(isbnToString!!).value!! == true) {
//                                Log.d(TAG, "3 ISBN true = $isbnToString")

                                //get books data from database
                                mFirebaseDatabaseBooks.child(isbnToString)
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(bookSnapshot: DataSnapshot) {
                                            val bookFromDatabase =
                                                bookSnapshot.getValue(BookModel::class.java)
//                                            Log.d(TAG, "4 ISBN number = ${bookFromDatabase?.isbn}")

                                            listData?.add(bookFromDatabase!!)
//                                            Log.d(TAG, "5 List Data $listData")
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.e(TAG, error.message)
                                        }
                                    })
                            }
                        }
                    }
                    GlobalScope.launch (Dispatchers.Main){
                        delay(1000)
//                        Log.d(TAG, "6 List Data = $listData")
                        mListBooksOwned.postValue(listData)
//                        Log.d(TAG, "7 mListBookOwned Data$mListBooksOwned")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, error.message)
                }
            })
    }

    fun getBooks(): LiveData<ArrayList<BookModel>> = mListBooksOwned
}