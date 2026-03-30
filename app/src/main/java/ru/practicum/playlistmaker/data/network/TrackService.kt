package ru.practicum.playlistmaker.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.playlistmaker.data.dto.TracksResponse

interface TrackService {
    @GET("/search?entity=song")
    fun findTrack(@Query("term") term: String): Call<TracksResponse>
}