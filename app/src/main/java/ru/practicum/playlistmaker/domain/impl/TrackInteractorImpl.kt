package ru.practicum.playlistmaker.domain.impl

import ru.practicum.playlistmaker.domain.api.TrackInteractor
import ru.practicum.playlistmaker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun findTracks(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.findTracks(expression))
        }
    }
}