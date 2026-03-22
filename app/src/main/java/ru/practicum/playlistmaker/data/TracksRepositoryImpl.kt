package ru.practicum.playlistmaker.data

import ru.practicum.playlistmaker.data.dto.TracksRequest
import ru.practicum.playlistmaker.data.dto.TracksResponse
import ru.practicum.playlistmaker.domain.api.TrackRepository
import ru.practicum.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient): TrackRepository  {
    override fun findTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksRequest(expression))
        if (response.resultCode == 200) {
            return (response as TracksResponse).results.map {
                Track(
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.previewUrl,
                    it.collectionName,
                    it.primaryGenreName,
                    it.releaseDate,
                    it.country
                )
            }
        } else {
            return emptyList()
        }
    }
}