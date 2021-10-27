package com.uni.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val library = BookList()

        library.add(BookModel("The Lord of the Rings (Paperback)", "J.R.R. Tolkien"))
        library.add(BookModel("Catch-22 (Paperback)", "Joseph Heller"))
        library.add(BookModel("A Gentleman in Moscow (Paperback)", "Amor Towles"))
        library.add(BookModel("The Hobbit, or There and Back Again (Paperback)", "J.R.R. Tolkien"))
        library.add(BookModel("Crime and Punishment (Paperback)", "Fyodor Dostoevsky"))
        library.add(BookModel("1984 (Kindle Edition)", "George Orwell"))
        library.add(BookModel("All the Light We Cannot See (Hardcover)", "Anthony Doerr"))
        library.add(BookModel("Blood Meridian, or the Evening Redness in the West (Paperback)", "Cormac McCarthy"))
        library.add(BookModel("One Hundred Years of Solitude (Mass Market Paperback)", "Gabriel García Márquez"))
        library.add(BookModel("The Screwtape Letters (Kindle Edition)", "C.S. Lewis"))
    }
}