package ru.practicum.playlistmaker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.playlistmaker.R
import ru.practicum.playlistmaker.layout.TrackViewHolder
import ru.practicum.playlistmaker.model.Track
import ru.practicum.playlistmaker.service.HistoryService

class TrackAdapter(private val tracks: List<Track>, private val historyService: HistoryService): RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { historyService.add(tracks[position]) }
    }

    override fun getItemCount(): Int = tracks.size

}