package com.uni.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val library = BookList()

        library.add(BookModel("The Lord of the Rings", "J.R.R. Tolkien"))
        library.add(BookModel("Catch-22", "Joseph Heller"))
        library.add(BookModel("A Gentleman in Moscow", "Amor Towles"))
        library.add(BookModel("The Hobbit, or There and Back Again", "J.R.R. Tolkien"))
        library.add(BookModel("Crime and Punishment", "Fyodor Dostoevsky"))
        library.add(BookModel("1984", "George Orwell"))
        library.add(BookModel("All the Light We Cannot See", "Anthony Doerr"))
        library.add(BookModel("Frankenstein", "Mary Wollstonecraft Shelley"))
        library.add(BookModel("One Hundred Years of Solitude", "Gabriel García Márquez"))
        library.add(BookModel("The Screwtape Letters", "C.S. Lewis"))

        Toast.makeText(applicationContext, library.size.toString(), Toast.LENGTH_SHORT).show()
        val bookListFragment = BookListFragment.newInstance(library)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_book_list_view, bookListFragment)
//                add<DisplayFragment>(R.id.fragment_container_view2)
            }
        }
    }
}