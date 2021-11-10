package com.uni.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val BOOKS = "BOOKS"
class BookListFragment : Fragment() {

    private val viewModel: BookViewModel by activityViewModels()
//    private lateinit var bookList: BookList
//
//    companion object {
//
//        fun newInstance(library: BookList) = BookListFragment().apply {
//            arguments = bundleOf(BOOKS to library)
//        }
//    }

    interface BookSelectedInterface {
        fun bookSelected()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        bookList = arguments?.getSerializable(BOOKS) as BookList
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onClick : (BookModel) -> Unit = {
            //Updating view model on book selection
            book: BookModel -> viewModel.setSelectedBook(book)
            //Informing activity to prevent replay of event when it restarts
            (activity as BookSelectedInterface).bookSelected()
        }

        val manager = LinearLayoutManager(activity)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)

        recyclerView?.layoutManager = manager

//        BookList.add(BookModel(0, "as", "sdf", "a"))
        recyclerView?.adapter = BookListAdapter(BookList, onClick)

    }
}