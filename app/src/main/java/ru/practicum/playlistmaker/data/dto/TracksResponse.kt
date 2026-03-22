package ru.practicum.playlistmaker.data.dto

import ru.practicum.playlistmaker.domain.models.Track

data class TracksResponse(val searchType: String,
                          val expression: String,
                          val results: List<Track>): Response()