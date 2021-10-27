package com.uni.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookViewModel : ViewModel(){

    private val bookData: MutableLiveData<BookModel> by lazy {
        MutableLiveData<BookModel>()
    }

    fun getBook(): LiveData<BookModel> {
        return bookData
    }

    fun setBook(movie:BookModel) {
        bookData.value = movie
    }
}