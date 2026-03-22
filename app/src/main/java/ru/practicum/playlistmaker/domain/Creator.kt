package ru.practicum.playlistmaker.domain

import ru.practicum.playlistmaker.data.TracksRepositoryImpl
import ru.practicum.playlistmaker.data.network.RetrofitNetworkClient
import ru.practicum.playlistmaker.domain.api.TrackInteractor
import ru.practicum.playlistmaker.domain.api.TrackRepository
import ru.practicum.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    private fun getMoviesRepository(): TrackRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): TrackInteractor {
        return TrackInteractorImpl(getMoviesRepository())
    }
}