package com.uni.audiobb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class BookAdapter(private val activity: MainActivity, private var list:BookList): RecyclerView.Adapter<BookAdapter.Viewholder>() {
    class Viewholder(itemView: View):RecyclerView.ViewHolder(itemView){
//        var textValue: TextView = itemView.findViewById(R.id.title)
//        var imageView: ImageView = itemView.findViewById(R.id.imageView)

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
//        holder.textValue.text = list[position].text
//        holder.imageView.setImageResource(list[position].imageId)
//
//        holder.itemView.setOnClickListener {
//            viewModel.setMovie(MovieModel(list[position].text,list[position].imageId))
//        }
    }
}