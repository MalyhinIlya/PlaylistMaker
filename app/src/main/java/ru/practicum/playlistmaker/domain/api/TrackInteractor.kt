package ru.practicum.playlistmaker.domain.api

import ru.practicum.playlistmaker.domain.models.Track

interface TrackInteractor {

    fun findTracks(expression: String, consumer: TrackConsumer)

    fun interface TrackConsumer {
        fun consume(tracks: List<Track>)
    }
}