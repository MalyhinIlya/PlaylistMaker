package ru.practicum.playlistmaker.data

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.playlistmaker.domain.api.HistoryRepository
import ru.practicum.playlistmaker.domain.models.Track
import androidx.core.content.edit
import ru.practicum.playlistmaker.data.dto.TrackDto
import kotlin.String

const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"

class HistoryRepositoryImpl(val sharedPrefs: SharedPreferences): HistoryRepository {

    override fun getHistory(): List<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "[]")
        return Gson().fromJson(json,  Array<TrackDto>::class.java).toList().map { dto ->
            Track(
                dto.trackName,
                dto.artistName,
                dto.trackTimeMillis,
                dto.artworkUrl100,
                dto.previewUrl,
                dto.collectionName,
                dto.primaryGenreName,
                dto.releaseDate,
                dto.country
            )
        }
    }

    override fun save(track: Track) {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "[]")
        val history = Gson().fromJson(json,  Array<Track>::class.java).toMutableList()
        history.remove(track)
        history.add(0, track)
        if (history.lastIndex > 9) history.removeAt(history.lastIndex)
        sharedPrefs.edit { putString(SEARCH_HISTORY_KEY, Gson().toJson(history)) }
    }

    override fun clear() {
        sharedPrefs.edit().remove(SEARCH_HISTORY_KEY).apply()
    }
}