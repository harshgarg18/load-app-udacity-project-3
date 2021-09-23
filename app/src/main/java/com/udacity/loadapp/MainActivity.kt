package com.udacity.loadapp

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.udacity.loadapp.databinding.ActivityMainBinding
import com.udacity.loadapp.ui.ButtonState
import com.udacity.loadapp.utils.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createNotificationChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )

        with(binding.contentMain.customButton) {
            setOnClickListener {
                if (buttonState == ButtonState.IDLE) {
                    download()
                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.download_completed),
                    Snackbar.LENGTH_SHORT
                ).show()
                val status = checkDownloadStatus(downloadID)
                sendNotification(
                    status,
                    getString(R.string.notification_channel_id),
                    downloadID.toInt(),
                    getString(R.string.action_details_text)
                )
                binding.contentMain.customButton.buttonState = ButtonState.IDLE
            }
        }
    }

    private fun download() {
        val downloadContent = when (binding.contentMain.radioGroup.checkedRadioButtonId) {
            R.id.glide_radio -> Triple(
                Constants.GLIDE_URL,
                getString(R.string.glide_download_title),
                getString(R.string.glide_download_text)
            )
            R.id.udacity_radio -> Triple(
                Constants.UDACITY_URL,
                getString(R.string.udacity_download_title),
                getString(R.string.udacity_download_text)
            )
            R.id.retrofit_radio -> Triple(
                Constants.RETROFIT_URL,
                getString(R.string.retrofit_download_title),
                getString(R.string.retrofit_download_text)
            )
            else -> Triple("", "", "")
        }


        if (downloadContent.first == "") {
            Toast.makeText(
                this,
                getString(R.string.no_file_selected_toast),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        binding.contentMain.customButton.buttonState = ButtonState.DOWNLOADING
        downloadID =
            downloadFile(downloadContent.first, downloadContent.second, downloadContent.third)
    }
}
