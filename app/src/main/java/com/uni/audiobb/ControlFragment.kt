package com.uni.audiobb

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import kotlin.math.roundToInt


class ControlFragment : Fragment() {

    lateinit var playerPosition:TextView
    lateinit var playerDuration:TextView
    lateinit var seekBar:SeekBar
    lateinit var btRew:ImageView
    lateinit var btPlay:ImageView
    lateinit var btPause:ImageView
    lateinit var btFf:ImageView
    lateinit var btStop:ImageView
    lateinit var btResume:ImageView
    lateinit var currentlyPlaying : TextView

    private val viewModel: BookViewModel by activityViewModels()

    interface PlayerControlInterface{
        fun play(id:Int)
        fun pause()
        fun seek(position: Double)
        fun forward()
        fun rewind()
        fun stop()
        fun resume()
        fun updateSavedProgress(id:String, position: Int)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerPosition = view.findViewById(R.id.player_position)
        playerDuration = view.findViewById(R.id.player_duration)
        seekBar = view.findViewById(R.id.seek_bar)
        btRew = view.findViewById(R.id.btn_rewind)
        btPlay = view.findViewById(R.id.btn_play)
        btPause = view.findViewById(R.id.btn_pause)
        btFf = view.findViewById(R.id.btn_ff)
        btStop = view.findViewById(R.id.btn_stop)
        btResume = view.findViewById(R.id.btn_resume)
        currentlyPlaying = view.findViewById(R.id.nowPlayingTextView)

        viewModel.getSelectedBook().observe(viewLifecycleOwner, { book ->

//            (activity as PlayerControlInterface).stop()

            if (book != null){
                currentlyPlaying.text = "Now Playing: ${book.title}..."
                playerDuration.text = timeElapsed(book.duration)

                btPlay.setOnClickListener {
                    btPlay.visibility = View.GONE
                    btPause.visibility = View.VISIBLE
                    btResume.visibility = View.GONE
                    (activity as PlayerControlInterface).play(book.id)
                    Log.d("apple", "play was clicked")
                }

                viewModel.getProg().observe(viewLifecycleOwner, { progress ->
                    val elapsed = ((progress.toDouble()/book.duration.toDouble()) * 100f).roundToInt()
//                    Log.d("apple", "$elapsed%")
                    (activity as PlayerControlInterface).updateSavedProgress(book.id.toString(), elapsed)
                    seekBar.progress = elapsed
                    playerPosition.text = timeElapsed(progress)
                    playerDuration.text = timeElapsed(book.duration)

                    if(elapsed == 100){
                        btPlay.visibility = View.VISIBLE
                        btPause.visibility = View.GONE
                        btResume.visibility = View.GONE
                    }
                })

                val progress = viewModel.getProg().value
                Log.d("apple", "CURRENT PROGRESS: $progress")
                if(progress == null) {
                    btPlay.visibility = View.VISIBLE
                    btPause.visibility = View.GONE
                    btResume.visibility = View.GONE
                }else{
                    btPlay.visibility = View.GONE
                    btPause.visibility = View.VISIBLE
                    btResume.visibility = View.GONE
                }



                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        if(fromUser){
                            Log.d("apple", "progress: $progress")
                            val newPosition = (progress.toDouble()/100f) * book.duration
                            (activity as PlayerControlInterface).seek(newPosition)
                        }
                    }
                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }
                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }
                })
            }
        })

        btPause.setOnClickListener {
            btPause.visibility = View.GONE
            btPlay.visibility = View.GONE
            btResume.visibility = View.VISIBLE

            (activity as PlayerControlInterface).pause()
            Log.d("apple", "pause was clicked")
        }

        btStop.setOnClickListener {
            btPlay.visibility = View.VISIBLE
            btPause.visibility = View.GONE
            btResume.visibility = View.GONE

            seekBar.progress = 0
            viewModel.setProg(0)
            (activity as PlayerControlInterface).stop()
        }

        btResume.setOnClickListener {
            btPause.visibility = View.VISIBLE
            btPlay.visibility = View.GONE
            btResume.visibility = View.GONE

            (activity as PlayerControlInterface).resume()
            Log.d("apple", "resume was clicked")
        }

        btFf.setOnClickListener {
            (activity as PlayerControlInterface).forward()
        }

        btRew.setOnClickListener {
            (activity as PlayerControlInterface).rewind()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_control, container, false)
    }

    private fun timeElapsed (elapsedTime:Int) : String{
        return DateUtils.formatElapsedTime(elapsedTime.toLong())
    }
}