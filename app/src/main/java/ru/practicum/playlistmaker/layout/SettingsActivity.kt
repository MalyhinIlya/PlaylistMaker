package ru.practicum.playlistmaker.layout

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import androidx.core.net.toUri
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.practicum.playlistmaker.App
import ru.practicum.playlistmaker.DARK_THEME_KEY
import ru.practicum.playlistmaker.PLAYLIST_MAKER_SHARED_PREFS
import ru.practicum.playlistmaker.R

const val PLAYLIST_MAKER_SHARED_PREFS = "playlist_maker_shared_prefs"
const val DARK_THEME_KEY = "DARK_THEME"
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_SHARED_PREFS, MODE_PRIVATE)
        setContentView(R.layout.activity_settings)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.back_to_main)
        toolbar.setTitleTextAppearance(this, R.style.header_style)
        toolbar.setNavigationOnClickListener { finish() }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)
        themeSwitcher.isChecked = sharedPrefs.getBoolean(DARK_THEME_KEY, (applicationContext as App).darkTheme)
        themeSwitcher.setOnCheckedChangeListener  {switcher, checked ->
            sharedPrefs.edit().putBoolean(DARK_THEME_KEY, checked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (checked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
        findViewById<TextView>(R.id.share_app).setOnClickListener {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.setType("text/plain")
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_link))
            startActivity(shareAppIntent)
        }

        findViewById<TextView>(R.id.need_help).setOnClickListener {
            val needHelpIntent = Intent(Intent.ACTION_SENDTO)
            needHelpIntent.data = "mailto:".toUri()
            needHelpIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_us)))
            needHelpIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.need_help_mail_subject))
            needHelpIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.need_help_mail_message))
            startActivity(needHelpIntent)
        }

        findViewById<TextView>(R.id.user_aggreement).setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            userAgreementIntent.data = getString(R.string.user_agreement_link).toUri()
            startActivity(userAgreementIntent)
        }

    }
}