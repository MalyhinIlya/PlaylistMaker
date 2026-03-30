package ru.practicum.playlistmaker.di.app

import android.content.SharedPreferences
import ru.practicum.playlistmaker.data.HistoryRepositoryImpl
import ru.practicum.playlistmaker.data.TracksRepositoryImpl
import ru.practicum.playlistmaker.data.network.RetrofitNetworkClient
import ru.practicum.playlistmaker.domain.api.TrackInteractor
import ru.practicum.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    private fun getTracksRepository() = TracksRepositoryImpl(RetrofitNetworkClient())

    fun provideTracksInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository())
    }

    fun getHistoryRepository(sharedPrefs: SharedPreferences) = HistoryRepositoryImpl(sharedPrefs)
}