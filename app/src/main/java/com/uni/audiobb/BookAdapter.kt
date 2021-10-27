package com.uni.audiobb

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        var isClick: Boolean

        holder.itemView.setOnClickListener {
            viewModel.setBook(list[position])
            isClick = true

            viewModel.getTwoPane().observe(activity, { twoPane ->

                Log.d("apple", "twoPane? $twoPane")
                if (!twoPane && isClick) {//if portrait
                    Log.d("apple", "portrait click! ${activity.supportFragmentManager.backStackEntryCount}")

                    activity.supportFragmentManager.commit {
                        replace(R.id.container1, viewModel.bookDetailsFragment, "detail").addToBackStack(null)
                    }
                    isClick = false
                }else if (twoPane && isClick){
                    Log.d("apple", "landscape click! ${activity.supportFragmentManager.backStackEntryCount}")
                    viewModel.bookDetailsFragment = BookDetailsFragment()
                    activity.supportFragmentManager.commit {
                        remove(viewModel.bookDetailsFragment)
                        add(R.id.container2, viewModel.bookDetailsFragment)
                    }
                    isClick = false
                }
            })
        }
    }
}