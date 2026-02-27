package ru.practicum.playlistmaker.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import ru.practicum.playlistmaker.layout.PlayerActivity
import ru.practicum.playlistmaker.R
import ru.practicum.playlistmaker.layout.TrackViewHolder
import ru.practicum.playlistmaker.model.Track
import ru.practicum.playlistmaker.service.HistoryService
import ru.practicum.playlistmaker.utils.Debouncer
import ru.practicum.playlistmaker.utils.Debouncer.Companion.clickDebounce

const val TRACK_KEY = "TRACK"

class TrackAdapter(private val tracks: List<Track>, private val historyService: HistoryService): RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                historyService.add(tracks[position])
                val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
                intent.putExtra(TRACK_KEY, Gson().toJson(tracks[position]))
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = tracks.size

}