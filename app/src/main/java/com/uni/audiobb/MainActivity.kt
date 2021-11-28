package com.uni.audiobb

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.commit
import edu.temple.audlibplayer.PlayerService

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

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
            Log.d("apple", "we have disconnected")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        if (!playerService.isPlaying){
            playerService.play(id)
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
}