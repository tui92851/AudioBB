package com.uni.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookListFragment : Fragment() {
    companion object {
        private const val BOOKS = "BOOKS"
        fun newInstance(library: BookList) = BookListFragment().apply {
            arguments = bundleOf(BOOKS to library)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_book_list, container, false)
        activity?.title = resources.getString(R.string.title)

        val manager = LinearLayoutManager(activity)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerview)

        recyclerView?.layoutManager = manager
        val library = arguments?.get("BOOKS") as BookList
        recyclerView?.adapter = BookAdapter(activity as MainActivity, library)
        return view
    }
}