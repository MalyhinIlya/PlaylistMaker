package ru.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.playlistmaker.model.Track
import ru.practicum.playlistmaker.model.TracksResponse

const val TRACKS_BASE_URL = "https://itunes.apple.com"

class SearchActivity : AppCompatActivity() {
    private var searchText = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var troubleView: LinearLayout
    private lateinit var troubleImage: ImageView
    private lateinit var troubleText: TextView
    private lateinit var refreshButton: Button
    private val retrofit = Retrofit.Builder()
        .baseUrl(TRACKS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackApi::class.java)
    private val tracks = mutableListOf<Track>()
    private val adapter = TrackAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        troubleText = findViewById(R.id.trouble_text)
        troubleImage = findViewById(R.id.trouble_image)
        troubleView = findViewById(R.id.trouble_layout)
        recyclerView = findViewById(R.id.track_view)

        val backButton = findViewById<MaterialToolbar>(R.id.back_to_main)
        backButton.setTitleTextAppearance(this, R.style.header_style)
        backButton.setNavigationOnClickListener { finish() }

        refreshButton = findViewById(R.id.refresh_button)
        refreshButton.setOnClickListener { refreshSearch() }

        val searchField = findViewById<EditText>(R.id.search_field)

        if(savedInstanceState != null) searchField.setText(savedInstanceState.getString(SEARCH_TEXT, ""))

        val clearTextButton = findViewById<ImageButton>(R.id.clear_text)

        searchField.addTextChangedListener(onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
            if (text.isNullOrEmpty()) {
                searchText = ""
                tracks.clear()
                adapter.notifyDataSetChanged()
                clearTextButton.visibility = GONE
                troubleView.visibility = GONE
            } else {
                searchText = text.toString()
                clearTextButton.visibility = VISIBLE
            }
        })

        clearTextButton.setOnClickListener {
            searchField.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            clearTextButton.visibility = GONE
            troubleView.visibility = GONE
            searchField.clearFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchField.windowToken, 0)
        }

        searchField.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val text = searchField.text
                if (text.isNullOrEmpty()) {
                    searchText = ""
                    clearTextButton.visibility = GONE
                } else {
                    searchText = text.toString()
                    clearTextButton.visibility = VISIBLE
                    searchTrack(searchText)
                }
                return@OnKeyListener true
            }
            false
        })

        recyclerView.adapter = adapter
    }

    private fun searchTrack(searchText: String) {
        trackService.findTrack(searchText).enqueue(object : Callback<TracksResponse> {
            override fun onResponse(call: Call<TracksResponse?>, response: Response<TracksResponse?>) {
                if (response.isSuccessful) {
                    tracks.clear()
                    val result = response.body()?.results
                    if (result?.isNotEmpty() == true) {
                        tracks.addAll(result)
                        adapter.notifyDataSetChanged()
                    }
                    if (tracks.isEmpty()) {
                        showMessage(false)
                    } else {
                        recyclerView.visibility = VISIBLE
                        troubleView.visibility = GONE
                    }
                } else {
                    showMessage(true)
                }
            }

            override fun onFailure(call: Call<TracksResponse?>, t: Throwable) {
                showMessage(true)
            }
        })
    }

    private fun showMessage(isError: Boolean) {
        recyclerView.visibility = GONE
        troubleView.visibility = VISIBLE
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
        searchTrack(searchText)
    }

    override fun onSaveInstanceState(
        outState: Bundle,
        outPersistentState: PersistableBundle
    ) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
}