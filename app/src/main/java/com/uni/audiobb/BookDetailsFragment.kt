package com.uni.audiobb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class BookDetailsFragment : Fragment() {

    private lateinit var bookTitle: TextView
    private lateinit var bookAuthor: TextView
    private val viewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_book_details, container, false)

        bookTitle = view.findViewById(R.id.titleTextView2)
        bookAuthor = view.findViewById(R.id.authorTextView2)

        viewModel.getBook().observe(viewLifecycleOwner, { book ->
            activity?.title = book.title
            bookTitle.text  = book.title
            bookAuthor.text = book.author
        })

        return view
    }
}