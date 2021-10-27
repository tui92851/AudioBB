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

class MainActivity : AppCompatActivity() {
    private val viewModel: BookViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.setTwoPane(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
        viewModel.setBook(BookModel("", ""))


        // This bit runs everytime the configuration changes
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction().
                add(R.id.container2, viewModel.bookDetailsFragment)
                .commit()
        }else{
            supportFragmentManager.popBackStack()
        }


        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.container1, viewModel.bookListFragment, "list")
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()

        viewModel.setBook(BookModel("", ""))
    }
}