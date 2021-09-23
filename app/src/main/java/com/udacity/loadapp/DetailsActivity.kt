package com.udacity.loadapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.loadapp.databinding.ActivityDetailsBinding
import com.udacity.loadapp.utils.Constants
import com.udacity.loadapp.utils.DownloadDetails
import com.udacity.loadapp.utils.DownloadStatus
import com.udacity.loadapp.utils.cancelNotification

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var downloadDetails: DownloadDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            onFabClicked()
        }

        val notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID_EXTRA, -1)
        cancelNotification(notificationId)

        downloadDetails = intent.getParcelableExtra(Constants.DETAILS_EXTRA) ?: return
        val title = downloadDetails.title
        val desc = downloadDetails.description
        val downloadStatus = downloadDetails.status

        with(binding.contentDetail) {
            if (downloadStatus == DownloadStatus.SUCCESS) {
                statusImage.setImageResource(R.drawable.ic_success)
                statusText.text = getString(R.string.download_success, title)
            } else {
                statusImage.setImageResource(R.drawable.ic_failure)
                statusText.text = getString(R.string.download_failure, title)
            }
            fileDesc.text = desc
        }

    }

    private fun onFabClicked() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
        finish()
    }

}
