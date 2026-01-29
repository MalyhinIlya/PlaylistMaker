package ru.practicum.playlistmaker.layout

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.playlistmaker.R
import ru.practicum.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val albumImg = itemView.findViewById<ImageView>(R.id.album_img)
    val artist = itemView.findViewById<TextView>(R.id.artist)
    val trackName = itemView.findViewById<TextView>(R.id.trackname)
    val trackDuration = itemView.findViewById<TextView>(R.id.track_duration)

    fun bind(track: Track) {
        albumImg.setBackgroundColor(Color.TRANSPARENT)
        Glide.with(itemView)
            .load(track.artworkUrl100).into(albumImg)
            .onLoadFailed(ContextCompat.getDrawable(itemView.context, R.drawable.placeholder))
        artist.text = if (track.artistName.length > 30) track.artistName.substring(0, 29) + "..." else track.artistName
        trackName.text = if (track.trackName.length > 30) track.trackName.substring(0, 29) + "..." else track.trackName
        trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
    }


}