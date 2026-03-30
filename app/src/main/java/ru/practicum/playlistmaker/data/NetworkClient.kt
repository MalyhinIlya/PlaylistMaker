package ru.practicum.playlistmaker.data

import ru.practicum.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}