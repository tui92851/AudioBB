package com.uni.audiobb
import java.io.Serializable

class BookList : Serializable{
    private val bookList : MutableList<BookModel> by lazy {
        ArrayList()
    }

    fun add(book: BookModel) {
        bookList.add(book)
    }

    fun remove(book: BookModel){
        bookList.remove(book)
    }

    operator fun get(index: Int) = bookList[index]

    fun size() = bookList.size

}