package ru.practicum.playlistmaker.model

data class Track (
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val previewUrl: String,
    val collectionName: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val country: String,
)
