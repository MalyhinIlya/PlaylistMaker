package ru.practicum.playlistmaker.service

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.playlistmaker.model.Track
import java.lang.reflect.Type


const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"
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
        tracks.clear()
        sharedPrefs.edit().remove(SEARCH_HISTORY_KEY).apply()
    }

    fun isEmpty() = sharedPrefs.getString(SEARCH_HISTORY_KEY, "")?.isEmpty()!!
}