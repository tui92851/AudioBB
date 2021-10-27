package com.uni.audiobb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView


class BookAdapter(private val activity: MainActivity, private var list:BookList, var viewModel: BookViewModel): RecyclerView.Adapter<BookAdapter.Viewholder>() {
    class Viewholder(itemView: View):RecyclerView.ViewHolder(itemView){
        var title: TextView = itemView.findViewById(R.id.titleTextView)
        var author: TextView = itemView.findViewById(R.id.authorTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.library_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.title.text = list[position].title
        holder.author.text = list[position].author

        holder.itemView.setOnClickListener {
            Toast.makeText(activity, list[position].title, Toast.LENGTH_SHORT).show()
            viewModel.setBook(list[position])

            val bookDetailsFragment = BookDetailsFragment()
            activity.supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_book_list_view, bookDetailsFragment).addToBackStack(null)
//                add(R.id.fragment_book_list_view, bookDetailsFragment).addToBackStack(null)
            }
        }
    }
}