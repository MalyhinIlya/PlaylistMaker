package ru.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val shareAppIntent = Intent(Intent.ACTION_SEND)
        shareAppIntent.setType("text/plain")
        shareAppIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_link))
        findViewById<TextView>(R.id.share_app).setOnClickListener {
            startActivity(shareAppIntent)
        }

        val needHelpIntent = Intent(Intent.ACTION_SENDTO)
        needHelpIntent.data = "mailto:".toUri()
        needHelpIntent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.contact_us))
        needHelpIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.need_help_mail_subject))
        needHelpIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.need_help_mail_message))
        findViewById<TextView>(R.id.need_help).setOnClickListener {
            startActivity(needHelpIntent)
        }

        val userAgreementIntent = Intent(Intent.ACTION_VIEW)
        userAgreementIntent.data = getString(R.string.user_agreement_link).toUri()
        findViewById<TextView>(R.id.user_aggreement).setOnClickListener {
            startActivity(userAgreementIntent)
        }

    }
}