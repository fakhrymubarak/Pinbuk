package com.fakhry.pinbuk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.pinbuk.R
import com.fakhry.pinbuk.model.BookModel
import kotlinx.android.synthetic.main.item_row_books.view.*

class BooksOwnedAdapter : RecyclerView.Adapter<BooksOwnedAdapter.ListViewHolder>() {
//    private lateinit var onItemClickCallback: ListUserAdapter.OnItemClickCallback
    private val listBooksOwned = ArrayList<BookModel>()

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(book: BookModel) {
            with(itemView) {
                tv_title.text = book.title
                tv_authors.text = book.author
                tv_category.text = book.category
                tv_year_published.text = book.year_published
                Glide.with(itemView.context)
                    .load(book.cover_url)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_refresh_24dp))
                    .error(R.drawable.ic_broken_image_24dp)
                    .into(iv_cover)
            }
        }
    }

    fun setData(items: ArrayList<BookModel>) {
        listBooksOwned.clear()
        listBooksOwned.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_books, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listBooksOwned[position])
//        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listBooksOwned[position]) }
    }

//    fun setOnItemClickCallback(onItemClickCallback: ListUserAdapter.OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }

    override fun getItemCount(): Int = listBooksOwned.size
}