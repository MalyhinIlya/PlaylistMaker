package ru.practicum.playlistmaker

import android.app.Application
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val albumImg = itemView.findViewById<ImageView>(R.id.album_img)
    val artist = itemView.findViewById<TextView>(R.id.artist)
    val trackName = itemView.findViewById<TextView>(R.id.trackname)
    val trackDuration = itemView.findViewById<TextView>(R.id.track_duration)

    fun bind(track: Track) {
        Glide.with(itemView).load(track.artworkUrl100).into(albumImg).onLoadFailed(
            ContextCompat.getDrawable(itemView.context, R.drawable.placeholder)
        )
        artist.text = track.artistName
        trackName.text = track.trackName
        trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
    }
}