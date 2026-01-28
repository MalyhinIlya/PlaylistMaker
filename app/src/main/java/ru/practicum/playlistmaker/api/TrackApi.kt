package ru.practicum.playlistmaker.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.playlistmaker.model.TracksResponse

interface TrackApi {
    @GET("/search?entity=song")
    fun findTrack(@Query("term") term: String): Call<TracksResponse>
}