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

    fun setBook(book:BookModel) {
        bookData.value = book
        bookData.postValue(bookData.value)
    }

    private val twoPane: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getTwoPane(): LiveData<Boolean> {
        return twoPane
    }

    fun setTwoPane(bool: Boolean) {
        twoPane.value = bool
    }

    val library = BookList().apply {
        add(BookModel("The Lord of the Rings", "J.R.R. Tolkien"))
        add(BookModel("Catch-22", "Joseph Heller"))
        add(BookModel("A Gentleman in Moscow", "Amor Towles"))
        add(BookModel("The Hobbit, or There and Back Again", "J.R.R. Tolkien"))
        add(BookModel("Crime and Punishment", "Fyodor Dostoevsky"))
        add(BookModel("1984", "George Orwell"))
        add(BookModel("All the Light We Cannot See", "Anthony Doerr"))
        add(BookModel("Frankenstein", "Mary Wollstonecraft Shelley"))
        add(BookModel("One Hundred Years of Solitude", "Gabriel García Márquez"))
        add(BookModel("The Screwtape Letters", "C.S. Lewis"))
    }

    val bookListFragment = BookListFragment.newInstance(library)
    var bookDetailsFragment = BookDetailsFragment()

}