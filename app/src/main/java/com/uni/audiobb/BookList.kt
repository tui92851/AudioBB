package com.uni.audiobb

import java.lang.RuntimeException

class BookList : ArrayList<BookModel>() {

    @JvmName("bookListSize")
    fun size(): Int {
        throw RuntimeException("Stub!")
    }

    override fun isEmpty(): Boolean {
        throw RuntimeException("Stub!")
    }

    override fun get(index: Int): BookModel {
        throw RuntimeException("Stub!")
    }

    override fun set(index: Int, element: BookModel): BookModel {
        throw RuntimeException("Stub!")
    }

    override fun add(element: BookModel): Boolean {
        throw RuntimeException("Stub!")
    }

    override fun remove(element: BookModel): Boolean {
        throw RuntimeException("Stub!")
    }

    override fun clear() {
        throw RuntimeException("Stub!")
    }
}