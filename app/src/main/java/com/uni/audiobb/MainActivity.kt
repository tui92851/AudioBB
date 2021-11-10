package com.uni.audiobb

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), BookListFragment.BookSelectedInterface{
    private val viewModel: BookViewModel by viewModels()
    private val isOnePane: Boolean by lazy {
        findViewById<View>(R.id.container2) == null
    }
    private lateinit var mainSearchButton: Button
    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                supportFragmentManager.commit {
                    replace(R.id.container1, BookListFragment())
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainSearchButton = findViewById(R.id.mainSearchButton)

        mainSearchButton.setOnClickListener {
            intentLauncher.launch(Intent(this, BookSearchActivity::class.java))
        }

        //clear book details fragment in container 1 if switching to two pane
        if (supportFragmentManager.findFragmentById(R.id.container1) is BookDetailsFragment) {
            supportFragmentManager.popBackStack()
        }

        //if twoPane but no book details fragment, add it to container 2
        if (!isOnePane && supportFragmentManager.findFragmentById(R.id.container2) !is BookDetailsFragment) {
            supportFragmentManager.commit {
                add(R.id.container2, BookDetailsFragment())
            }
        }
        //if activity is loading for first time, add the book list fragment
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.container1, BookListFragment())
            }
        } else if (isOnePane && viewModel.getSelectedBook().value != null) {
            //if activity has loaded a booklist fragment already, place single container on top
            supportFragmentManager.commit {
                replace(R.id.container1, BookDetailsFragment())
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }

    override fun onBackPressed() {
        viewModel.setSelectedBook(null)
        super.onBackPressed()
    }

    override fun bookSelected() {
        if (isOnePane) {
            supportFragmentManager.commit {
                replace(R.id.container1, BookDetailsFragment())
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }
}