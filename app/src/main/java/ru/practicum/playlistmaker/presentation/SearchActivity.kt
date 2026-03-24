package ru.practicum.playlistmaker.presentation

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import ru.practicum.playlistmaker.R
import ru.practicum.playlistmaker.di.app.Creator
import ru.practicum.playlistmaker.domain.PLAYLIST_MAKER_SHARED_PREFS
import ru.practicum.playlistmaker.domain.api.HistoryRepository
import ru.practicum.playlistmaker.domain.models.Track
import ru.practicum.playlistmaker.presentation.track.TrackAdapter

class SearchActivity : AppCompatActivity() {
    private val searchRunnable = Runnable { searchTrack() }
    private val handler = Handler(Looper.getMainLooper())
    private var searchText = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var troubleView: LinearLayout
    private lateinit var troubleImage: ImageView
    private lateinit var troubleText: TextView
    private lateinit var refreshButton: Button
    private lateinit var backButton: MaterialToolbar
    private lateinit var clearTextButton: ImageButton
    private lateinit var searchField: EditText
    private lateinit var youSearch: TextView
    private lateinit var clearHistory: MaterialButton
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var adapter: TrackAdapter

    private lateinit var historyRepository: HistoryRepository
    private lateinit var progressBar: ProgressBar
    private val tracks = mutableListOf<Track>()

    fun init() {
        troubleText = findViewById(R.id.trouble_text)
        troubleImage = findViewById(R.id.trouble_image)
        troubleView = findViewById(R.id.trouble_layout)
        recyclerView = findViewById(R.id.track_view)

        backButton = findViewById(R.id.back_to_main)
        backButton.setTitleTextAppearance(this, R.style.header_style)
        backButton.setNavigationOnClickListener { finish() }

        refreshButton = findViewById(R.id.refresh_button)
        refreshButton.setOnClickListener { refreshSearch() }

        clearTextButton = findViewById(R.id.clear_text)
        searchField = findViewById(R.id.search_field)
        youSearch = findViewById(R.id.you_search)
        clearHistory = findViewById(R.id.clear_history)

        progressBar = findViewById(R.id.progressBar)

        sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_SHARED_PREFS, MODE_PRIVATE)
        historyRepository = Creator.getHistoryRepository(sharedPrefs)
        adapter = TrackAdapter(tracks, historyRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        if(savedInstanceState != null)
            searchField.setText(savedInstanceState.getString(SEARCH_TEXT, ""))

        searchField.addTextChangedListener(onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
            onChangeText(text)
        })

        searchField.setOnFocusChangeListener { view, hasFocus ->
            val history = historyRepository.getHistory()
            if (hasFocus && searchField.text.isEmpty() && !history.isEmpty()) {
                youSearch.visibility = VISIBLE
                recyclerView.visibility = VISIBLE
                clearHistory.visibility = VISIBLE
                tracks.clear()
                tracks.addAll(history)
                adapter.notifyDataSetChanged()
            } else {
                youSearch.visibility = GONE
                clearHistory.visibility = GONE
            }
        }

        clearTextButton.setOnClickListener {
            onClearTextButtonClick()
        }

        clearHistory.setOnClickListener {
            tracks.clear()
            historyRepository.clear()
            adapter.notifyDataSetChanged()
            hideHistoryView()
        }

        recyclerView.adapter = adapter
    }

    private fun hideHistoryView() {
        youSearch.visibility = GONE
        clearHistory.visibility = GONE
    }

    private fun onClearTextButtonClick() {
        searchField.setText("")
        tracks.clear()
        adapter.notifyDataSetChanged()
        clearTextButton.visibility = GONE
        troubleView.visibility = GONE
        searchField.clearFocus()
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(searchField.windowToken, 0)
    }

    private fun onChangeText(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            searchText = ""
            tracks.clear()
            val history = historyRepository.getHistory()
            if (!history.isEmpty()) {
                tracks.addAll(history)
                youSearch.visibility = VISIBLE
                clearHistory.visibility = VISIBLE
                progressBar.visibility = GONE
                adapter.notifyDataSetChanged()
            }
            clearTextButton.visibility = GONE
            troubleView.visibility = GONE
        } else {
            tracks.clear()
            searchText = text.toString()
            clearTextButton.visibility = VISIBLE
            youSearch.visibility = GONE
            clearHistory.visibility = GONE
            progressBar.visibility = VISIBLE
            searchDebounce()
        }
    }

    private fun searchTrack() {
        progressBar.visibility = VISIBLE
        if (!searchText.isEmpty()) {;
            Creator.provideTracksInteractor().findTracks(searchText, {
                handler.post {
                    if (it.isFailure) showMessage(true)
                    else {
                        tracks.clear()
                        tracks.addAll(it.getOrNull() ?: emptyList())
                        recyclerView.visibility = VISIBLE
                        adapter.notifyDataSetChanged()
                        if (tracks.isEmpty()) {
                            showMessage(false)
                        } else {
                            recyclerView.visibility = VISIBLE
                            troubleView.visibility = GONE
                        }
                        progressBar.visibility = GONE
                    }
                }
            })
        }
        progressBar.visibility = GONE
    }

    private fun showMessage(isError: Boolean) {
        recyclerView.visibility = GONE
        troubleView.visibility = VISIBLE
        progressBar.visibility = GONE
        if (isError) {
            troubleImage.setImageResource(R.drawable.get_tracks_error)
            troubleText.text = getString(R.string.get_tracks_error_message)
            refreshButton.visibility = VISIBLE
        } else {
            troubleImage.setImageResource(R.drawable.tracks_empty)
            troubleText.text = getString(R.string.empty_search_result)
            refreshButton.visibility = GONE
        }
    }

    private fun refreshSearch() {
        searchTrack()
    }

    override fun onSaveInstanceState(
        outState: Bundle,
        outPersistentState: PersistableBundle
    ) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}