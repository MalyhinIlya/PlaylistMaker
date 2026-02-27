package ru.practicum.playlistmaker.utils

import android.os.Handler
import android.os.Looper
import android.util.Log

class Debouncer() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private var isClickAllowed = true

        private val handler = Handler(Looper.getMainLooper())

        fun clickDebounce() : Boolean {
            val current = isClickAllowed
            if (isClickAllowed) {
                isClickAllowed = false
                handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
            }
            return current
        }
    }
}

