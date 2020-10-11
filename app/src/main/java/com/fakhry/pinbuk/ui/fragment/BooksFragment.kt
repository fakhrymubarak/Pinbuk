package com.fakhry.pinbuk.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.adapter.BooksBorrowingAdapter
import com.fakhry.pinbuk.adapter.BooksOwnedAdapter
import com.fakhry.pinbuk.viewmodel.BooksBorrowingViewModel
import com.fakhry.pinbuk.viewmodel.BooksOwnedViewModel
import kotlinx.android.synthetic.main.fragment_books.*

class BooksFragment : Fragment() {
    private lateinit var booksOwnedAdapter: BooksOwnedAdapter
    private lateinit var booksBorrowingAdapter: BooksBorrowingAdapter

    private lateinit var booksOwnedViewModel: BooksOwnedViewModel
    private lateinit var booksBorrowingViewModel: BooksBorrowingViewModel

    companion object {
        private const val ARG_UID = "user_uid"
        private const val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(index: Int, uid: String?): BooksFragment {
            val fragment = BooksFragment()
            val bundle = Bundle()
            bundle.putString(ARG_UID, uid)
            bundle.putInt(ARG_SECTION_NUMBER, index)

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = arguments?.getString(ARG_UID)
        progress_bar.visibility = View.VISIBLE
        val itemDecor = DividerItemDecoration(context, RecyclerView.VERTICAL)
        rv_books.addItemDecoration(itemDecor)

        rv_books.setHasFixedSize(true)
        rv_books.layoutManager = LinearLayoutManager(activity)

        if (arguments != null) {
            when (arguments?.getInt(ARG_SECTION_NUMBER, 0) ?: 1) {
                1 -> {
                    booksOwnedAdapter = BooksOwnedAdapter()
                    rv_books.adapter = booksOwnedAdapter

                    booksOwnedViewModel = ViewModelProvider(
                        this,
                        ViewModelProvider.NewInstanceFactory()
                    ).get(BooksOwnedViewModel::class.java)
                    booksOwnedViewModel.takeListBooksOwnedFromDatabase(uid!!)
                    booksOwnedViewModel.getBooks().observe(viewLifecycleOwner, { bookModel ->
                        if (bookModel != null) {
                            booksOwnedAdapter.setData(bookModel)
                        } else {
                            Toast.makeText(context, "You don't have any books.", Toast.LENGTH_LONG)
                                .show()
                        }
                        progress_bar.visibility = View.GONE
                    })

                }
                2 -> {
                    booksBorrowingAdapter = BooksBorrowingAdapter()
                    rv_books.adapter = booksBorrowingAdapter

                    booksBorrowingViewModel = ViewModelProvider(
                        this,
                        ViewModelProvider.NewInstanceFactory()
                    ).get(BooksBorrowingViewModel::class.java)
                    booksBorrowingViewModel.takeListBooksBorrowingFromDatabase(uid!!)
                    booksBorrowingViewModel.getBooks().observe(viewLifecycleOwner, { bookModel ->
                        if (bookModel != null) {
                            booksBorrowingAdapter.setData(bookModel)
                        } else {
                            Toast.makeText(
                                context,
                                "You don't borrow any books.",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                        progress_bar.visibility = View.GONE

                    })
//                    booksBorrowingAdapter.setOnItemClickCallback(object :
//                        ListUserAdapter.OnItemClickCallback {
//                        override fun onItemClicked(data: UserModel) {
//                            showSelectedUser(data)
//                        }
//                    })
                }
            }
        }
    }

//    private fun showSelectedUser(userModel: UserModel) {
//        val intent = Intent(context, DetailActivity::class.java)
//        intent.putExtra(DetailActivity.EXTRA_STATE, userModel)
//        startActivity(intent)
//    }
}