package ru.practicum.playlistmaker.data

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.playlistmaker.domain.models.Track


class HistoryService(val sharedPrefs: SharedPreferences, val tracks: MutableList<Track>) {

    fun showHistory() {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "[]")
        tracks.clear()
        tracks.addAll(Gson().fromJson(json,  Array<Track>::class.java))
    }

    fun add(track: Track) {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "[]")
        val history = Gson().fromJson(json,  Array<Track>::class.java).toMutableList()
        history.remove(track)
        history.add(0, track)
        if (history.lastIndex > 9) history.removeAt(history.lastIndex)
        sharedPrefs.edit().putString(SEARCH_HISTORY_KEY, Gson().toJson(history)).apply()
    }

    fun clearHistory() {
    }

    fun isEmpty() = sharedPrefs.getString(SEARCH_HISTORY_KEY, "")?.isEmpty()!!
}