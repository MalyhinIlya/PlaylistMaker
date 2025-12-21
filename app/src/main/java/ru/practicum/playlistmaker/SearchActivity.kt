package ru.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arraySetOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.google.android.material.appbar.MaterialToolbar
import ru.practicum.playlistmaker.model.MockTracks

class SearchActivity : AppCompatActivity() {
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<MaterialToolbar>(R.id.back_to_main)
        backButton.setTitleTextAppearance(this, R.style.header_style)
        backButton.setNavigationOnClickListener { finish() }

        val searchField = findViewById<EditText>(R.id.search_field)

        if(savedInstanceState != null) searchField.setText(savedInstanceState.getString(SEARCH_TEXT, ""))
        val clearTextButton = findViewById<ImageButton>(R.id.clear_text)
        clearTextButton.setOnClickListener {
            searchField.setText("")
            searchField.clearFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchField.windowToken, 0)
        }

        searchField.addTextChangedListener(onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
            if (text.isNullOrEmpty()) {
                searchText = ""
                clearTextButton.visibility = GONE
            } else {
                searchText = text.toString()
                clearTextButton.visibility = VISIBLE
            }
        })

        val recyclerView = findViewById<RecyclerView>(R.id.track_view)
        val adapter = TrackAdapter(MockTracks.tracks)
        recyclerView.adapter = adapter
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