package ru.practicum.playlistmaker.domain.api

import ru.practicum.playlistmaker.domain.models.Track

interface TrackRepository {
    fun findTracks(expression: String): List<Track>
}