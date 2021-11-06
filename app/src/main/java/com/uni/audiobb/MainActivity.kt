package com.uni.audiobb

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity(), BookListFragment.BookSelectedInterface {
    private val viewModel: BookViewModel by viewModels()
    private val isOnePane : Boolean by lazy{
        findViewById<View>(R.id.container2) == null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val library = viewModel.library

        //clear book details fragment in container 1 if switching to two pane
        if (supportFragmentManager.findFragmentById(R.id.container1) is BookDetailsFragment){
            supportFragmentManager.popBackStack()
        }

        //if activity is loading for first time, add the book list fragment
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.container1, BookListFragment.newInstance(library))
            }
        }else if(isOnePane && viewModel.getSelectedBook().value != null){
            //if activity has loaded a booklist fragment already, place single container on top
            supportFragmentManager.commit {
                replace(R.id.container1, BookDetailsFragment())
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }

        //if twoPane but no book details fragment, add it to container 2
        if(!isOnePane && supportFragmentManager.findFragmentById(R.id.container2) !is BookDetailsFragment){
            supportFragmentManager.commit {
                add(R.id.container2, BookDetailsFragment())
            }
        }
    }

    override fun onBackPressed() {
        viewModel.setSelectedBook(null)
        super.onBackPressed()
    }

    override fun bookSelected() {
        if(isOnePane){
            supportFragmentManager.commit {
                replace(R.id.container1, BookDetailsFragment())
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }
}