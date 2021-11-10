package com.uni.audiobb

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.lang.ref.WeakReference

class BookSearchActivity : AppCompatActivity() {
    private lateinit var searchField:EditText
    private lateinit var searchButton: Button
    private lateinit var closeButton: Button
    private val client = OkHttpClient()
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_book_search)
        this.setFinishOnTouchOutside(false)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.INTERNET
            ), 1
        )


        searchField = findViewById(R.id.searchBar)
        searchButton = findViewById(R.id.searchButton)
        closeButton = findViewById(R.id.closeButton)

        searchButton.setOnClickListener {
            val response = httpRequest("https://kamorris.com/lab/cis3515/search.php?term=${searchField.text}")
            val moshi = Moshi.Builder().build()
            val listType = Types.newParameterizedType(List::class.java, BookModel::class.java)
            val adapter: JsonAdapter<List<BookModel>> = moshi.adapter(listType)
            val library: List<BookModel>? = adapter.fromJson(response?.body?.source()!!)
//            Toast.makeText(this, library?.size.toString(), Toast.LENGTH_SHORT  ).show()
            if (library != null) {
                BookList.clear()
                BookList.addLibrary(library)
            }

//            viewModel.listener.onLibraryUpdated()

            Toast.makeText(this, BookList.size().toString(), Toast.LENGTH_SHORT).show()
        }

        closeButton.setOnClickListener{
            this.finish()
        }


    }

    private fun httpRequest(url: String): Response? {
        val request = Request.Builder()
            .url(url)
            .build()

        var response: Response? = null

        runBlocking(Dispatchers.IO) {
            response = client.newCall(request).execute()
//            result = response?.body?.string().toString()
        }

        return response
    }


}