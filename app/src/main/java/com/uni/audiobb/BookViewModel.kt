package com.uni.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import edu.temple.audlibplayer.PlayerService

class BookViewModel : ViewModel(){

    private val book: MutableLiveData<BookModel> by lazy {
        MutableLiveData()
    }

    fun getSelectedBook(): LiveData<BookModel> {
        return book
    }

    fun setSelectedBook(selectedBook: BookModel?) {
        this.book.value = selectedBook
    }


    private val progress: MutableLiveData<PlayerService.BookProgress> by lazy {
        MutableLiveData()
    }

    fun getProg(): LiveData<PlayerService.BookProgress> {
        return progress
    }

    fun setProg(progress: PlayerService.BookProgress?) {
        this.progress.value = progress
    }
}