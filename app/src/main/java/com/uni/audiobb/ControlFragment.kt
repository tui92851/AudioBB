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
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import edu.temple.audlibplayer.PlayerService
import kotlin.math.roundToInt


class ControlFragment : Fragment() {

    lateinit var playerPosition:TextView
    lateinit var playerDuration:TextView
    lateinit var seekBar:SeekBar
    lateinit var btRew:ImageView
    lateinit var btPlay:ImageView
    lateinit var btPause:ImageView
    lateinit var btFf:ImageView
    lateinit var stop:ImageView

    private val viewModel: BookViewModel by activityViewModels()
//    companion object {
//        private const val MEDIAPLAYER = "mediaplayer"
//
//        fun newInstance(player: PlayerService.MediaControlBinder) = ControlFragment().apply {
//            arguments = bundleOf(
//                MEDIAPLAYER to player
//            )
//        }
//    }

    interface PlayerControlInterface{
        fun play(id:Int)
        fun pause()
        fun seek(position: Double)
        fun forward()
        fun rewind()
        fun stop()
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
        stop = view.findViewById(R.id.btn_stop)

        viewModel.getSelectedBook().observe(viewLifecycleOwner, { book ->

            playerDuration.text = timeElapsed(book.duration)

            viewModel.getProg().observe(viewLifecycleOwner, { prog ->
                val elapsed = ((prog.progress.toDouble()/book.duration.toDouble()) * 100f).roundToInt()
                Log.d("apple", "$elapsed%")
                seekBar.progress = elapsed
                playerPosition.text = timeElapsed(prog.progress)
                playerDuration.text = timeElapsed(book.duration)
            })

            btPlay.setOnClickListener {
                btPlay.visibility = View.GONE
                btPause.visibility = View.VISIBLE
                (activity as PlayerControlInterface).play(book.id)
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

        })

        btPause.setOnClickListener {
            btPause.visibility = View.GONE
            btPlay.visibility = View.VISIBLE
            (activity as PlayerControlInterface).pause()
        }

        stop.setOnClickListener {
            (activity as PlayerControlInterface).stop()
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