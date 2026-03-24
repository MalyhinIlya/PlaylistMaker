package ru.practicum.playlistmaker.data.dto

data class TracksResponse(val searchType: String,
                          val expression: String,
                          val results: List<TrackDto>): Response()