package ru.practicum.playlistmaker.layout

import android.os.Bundle
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
import java.time.LocalDateTime
import java.util.Locale

const val TRACK_KEY = "TARCK"

class PlayerActivity : AppCompatActivity() {

    private lateinit var trackName: TextView
    private lateinit var artist: TextView
    private lateinit var albumImg: ImageView
    private lateinit var album: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var duration: TextView
    private lateinit var country: TextView
    private lateinit var backButton: MaterialToolbar


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
        val track = Gson().fromJson(intent.getStringExtra(TRACK_KEY), Track::class.java)

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
}