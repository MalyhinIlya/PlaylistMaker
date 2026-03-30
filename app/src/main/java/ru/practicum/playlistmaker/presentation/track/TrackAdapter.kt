package ru.practicum.playlistmaker.presentation.track

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import ru.practicum.playlistmaker.R
import ru.practicum.playlistmaker.domain.models.Track
import ru.practicum.playlistmaker.presentation.PlayerActivity
import ru.practicum.playlistmaker.domain.Debouncer.Companion.clickDebounce
import ru.practicum.playlistmaker.domain.api.HistoryRepository

const val TRACK_KEY = "TRACK"

class TrackAdapter(private val tracks: List<Track>, private val historyRepository: HistoryRepository): RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                historyRepository.save(tracks[position])
                val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
                intent.putExtra(TRACK_KEY, Gson().toJson(tracks[position]))
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = tracks.size

}