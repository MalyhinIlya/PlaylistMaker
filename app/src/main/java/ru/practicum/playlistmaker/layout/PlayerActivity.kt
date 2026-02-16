package ru.practicum.playlistmaker.layout

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import ru.practicum.playlistmaker.R
import ru.practicum.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

const val TRACK_KEY = "TRACK"

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private lateinit var trackName: TextView
    private lateinit var artist: TextView
    private lateinit var albumImg: ImageView
    private lateinit var album: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var duration: TextView
    private lateinit var country: TextView
    private lateinit var backButton: MaterialToolbar
    private lateinit var playButton: ImageView
    private lateinit var timePlayed: TextView

    private lateinit var track: Track

    private var handler: Handler? = null

    private val runnable = object : Runnable {
        override fun run() {
            showPlayingPosition()
            handler?.postDelayed(this, 300)
        }
    }

    private var mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT

    private fun init() {
        trackName = findViewById(R.id.track_name)
        artist = findViewById(R.id.artist)
        albumImg = findViewById(R.id.album_img)
        album = findViewById(R.id.album)
        year = findViewById(R.id.year)
        genre = findViewById(R.id.genre)
        duration = findViewById(R.id.duration)
        country = findViewById(R.id.country)

        backButton = findViewById(R.id.back_to_main)
        backButton.setTitleTextAppearance(this, R.style.header_style)
        backButton.setNavigationOnClickListener { finish() }

        playButton = findViewById(R.id.play)
        timePlayed = findViewById(R.id.time_played)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        init()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handler = Handler(Looper.getMainLooper())
        track = Gson().fromJson(intent.getStringExtra(TRACK_KEY), Track::class.java)

        showTrackInfo()
        preparePlayer()

        playButton.setOnClickListener {
            if (playerState == STATE_PREPARED || playerState == STATE_PAUSED)
                startPlayer()
            else if (playerState == STATE_PLAYING)
                pausePlayer()
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { playerState = STATE_PREPARED }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            playButton.setImageResource(R.drawable.play)
        }
    }

    private fun showTrackInfo() {

        Glide.with(albumImg)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")).into(albumImg)
            .onLoadFailed(ContextCompat.getDrawable(albumImg.context, R.drawable.placeholder))

        trackName.text = track.trackName
        artist.text = track.artistName
        album.text = track.collectionName
        year.text = track.releaseDate.subSequence(0, 4)
        genre.text = track.primaryGenreName
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        country.text = track.country
    }

    private fun startPlayer() {
        mediaPlayer.start()
        showPlayingPosition()
        playerState = STATE_PLAYING
        playButton.setImageResource(R.drawable.pause)

        showPlayingPosition()

        handler?.post(runnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playButton.setImageResource(R.drawable.play)
    }

    private fun showPlayingPosition() {
        Log.i("TrackPosition", "Current position is " + mediaPlayer.currentPosition / 60)
        timePlayed.text = SimpleDateFormat("m:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(runnable)
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}
