package com.uni.audiobb

import android.Manifest
import android.R.attr
import android.app.Activity
import android.app.DownloadManager
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import edu.temple.audlibplayer.PlayerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import android.R.attr.data
import android.content.*
import android.content.pm.PackageManager
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter


class MainActivity : AppCompatActivity(), BookListFragment.BookSelectedInterface, ControlFragment.PlayerControlInterface{
    private val viewModel: BookViewModel by viewModels()
    private val isOnePane: Boolean by lazy {
        findViewById<View>(R.id.container2) == null
    }
    private lateinit var mainSearchButton: ImageView
    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                supportFragmentManager.commit {
                    replace(R.id.container1, BookListFragment())
                }
            }
        }

    lateinit var playerService: PlayerService.MediaControlBinder
    var isConnected = false
    val playerHandler = object:  Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.obj != null){
                viewModel.setProg((msg.obj as PlayerService.BookProgress).progress)
            }
        }
    }

    private lateinit var controlFragment: ControlFragment

    private val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isConnected = true
            Log.d("apple", "we have connected")
            playerService = service as PlayerService.MediaControlBinder
            playerService.setProgressHandler(playerHandler)
//            playerService.play(sharedpref.getInt("currently playing", 0))

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
            Log.d("apple", "we have disconnected")
        }

    }

    private lateinit var sharedpref : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedpref = getSharedPreferences("BOOK_PROGRESS", Context.MODE_PRIVATE)
        editor = sharedpref.edit()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), 1
        )


        controlFragment = ControlFragment()
        supportFragmentManager.commit {
            add(R.id.playerContainer, controlFragment)
//                hide(controlFragment)
        }

        mainSearchButton = findViewById(R.id.search_button)



        mainSearchButton.setOnClickListener {
            intentLauncher.launch(Intent(this, BookSearchActivity::class.java))
        }

        //clear book details fragment in container 1 if switching to two pane
        if (supportFragmentManager.findFragmentById(R.id.container1) is BookDetailsFragment) {

            supportFragmentManager.popBackStack()
//            supportFragmentManager.popBackStack()
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

        bindService(Intent(this, PlayerService::class.java)
            , serviceConnection
            , BIND_AUTO_CREATE
        )


    }

    override fun onBackPressed() {
        viewModel.setSelectedBook(null)
        super.onBackPressed()
    }

    override fun bookSelected(book: BookModel) {
        Log.d("apple", book.title + " was selected")
        if (isOnePane) {
            supportFragmentManager.commit {
                replace(R.id.container1, BookDetailsFragment())
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }

//        if (playerService.isPlaying){
//            playerService.stop()
//        }
        supportFragmentManager.commit {
            remove(controlFragment)
            add(R.id.playerContainer, controlFragment)
        }
    }

    override fun play(id: Int) {

        val bookFileName = "${viewModel.getSelectedBook().value!!.title}.mp3"
        val file = File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),bookFileName)

        editor.putInt("1", 60)
        if (!playerService.isPlaying){

            if (file.exists()){
                Log.d("apple", "this file exists ${id}. It is starting at ${sharedpref.getInt(id.toString(), 0)}.")
                Toast.makeText(this, "playing audiobook from local storage", Toast.LENGTH_SHORT).show()
                playerService.play(file, 0)
            }else{
                Log.d("apple", "this file doesn't exist. ${file.absolutePath}")
                Toast.makeText(this, "downloading audiobook", Toast.LENGTH_SHORT).show()
                playerService.play(id)
                lifecycleScope.launch(Dispatchers.IO){
                    Log.d("apple", "starting download")
                    val url = "https://kamorris.com/lab/audlib/download.php?id=${viewModel.getSelectedBook().value!!.id}"
                    Log.d("apple", "download url: $url")

                    if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Log.d("apple", "permissions were granted")
                        download(this@MainActivity, url, viewModel.getSelectedBook().value!!.title)
                    }else{
                        Log.d("apple", "permissions were not granted")
                    }

                    Log.d("apple", "download finished")
                }
            }
        }
    }

    override fun pause() {
        if(playerService.isPlaying) {
            playerService.pause()
        }
    }

    override fun seek(position: Double) {
        if(playerService.isPlaying) {
            playerService.seekTo(position.toInt())
//            Log.d("apple", "this books saved position is ${sharedpref.getInt("1", 0)}")
        }
    }

    override fun forward() {
        if(playerService.isPlaying){
            viewModel.getProg().value?.let { it1 -> playerService.seekTo(it1 + 10) }
        }
    }

    override fun rewind() {
        if(playerService.isPlaying){
            viewModel.getProg().value?.let { it1 -> playerService.seekTo(it1 - 10) }
        }
    }

    override fun stop() {
        if (playerService.isPlaying) {
            playerService.stop()
            //when stop is pressed while book is playing, its progress is set to 0
            editor.putInt(viewModel.getSelectedBook().value!!.id.toString(), 0)
        }
    }

    override fun resume() {
        if(!playerService.isPlaying) {
            val position = viewModel.getProg().value
            Log.d("apple", "OUR CURRENT POSITION $position")
            if (position != null) {
                playerService.seekTo(position)
            }

            playerService.pause()
        }
    }

    override fun updateSavedProgress(id: String, position: Int) {
        editor.putInt(id, position)
        editor.putInt("currently playing", Integer.parseInt(id))
    }

    fun download(baseActivity:Context,url: String?,title: String?){
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
//        request.setTitle(title)
        request.setMimeType("application/mp3")
//        request.addRequestHeader("User-Agent", System.getProperty("http.agent"))
        request.setAllowedOverMetered(true)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(baseActivity, Environment.DIRECTORY_DOWNLOADS, "$title.mp3")
        val  dm: DownloadManager = baseActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
    }
}