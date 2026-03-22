package ru.practicum.playlistmaker.domain.api

import ru.practicum.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun getHistory(): List<Track>
    fun save(track: Track)
    fun clear()
}