package ru.practicum.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.playlistmaker.data.NetworkClient
import ru.practicum.playlistmaker.data.dto.Response
import ru.practicum.playlistmaker.data.dto.TracksRequest
import ru.practicum.playlistmaker.data.dto.TracksResponse
import ru.practicum.playlistmaker.presentation.TRACKS_BASE_URL

class RetrofitNetworkClient: NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(TRACKS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService = retrofit.create(TrackService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TracksRequest) {
            val resp = trackService.findTrack(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}