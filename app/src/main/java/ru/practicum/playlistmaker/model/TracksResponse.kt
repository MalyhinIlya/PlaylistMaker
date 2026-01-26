package ru.practicum.playlistmaker.model

class TracksResponse(val searchType: String,
                     val expression: String,
                     val results: List<Track>)