package com.uni.audiobb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class BookAdapter(private val activity: MainActivity, private var list:BookList): RecyclerView.Adapter<BookAdapter.Viewholder>() {
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
        Toast.makeText(activity, position.toString(), Toast.LENGTH_SHORT).show()
        holder.title.text = list[position].title
        holder.author.text = list[position].author
//
//        holder.itemView.setOnClickListener {
//            viewModel.setMovie(MovieModel(list[position].text,list[position].imageId))
//        }
    }
}