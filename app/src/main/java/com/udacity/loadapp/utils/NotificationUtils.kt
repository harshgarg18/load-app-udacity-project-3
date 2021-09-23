package com.udacity.loadapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.udacity.loadapp.DetailsActivity
import com.udacity.loadapp.R

fun Context.createNotificationChannel(channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            enableVibration(true)
            setShowBadge(false)
            lightColor = Color.RED
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

fun Context.sendNotification(
    downloadDetails: DownloadDetails,
    channelId: String,
    notificationId: Int,
    actionText: String
) {
    val actionIntent = Intent(this, DetailsActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtra(Constants.NOTIFICATION_ID_EXTRA, notificationId)
        putExtra(Constants.DETAILS_EXTRA, downloadDetails)
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        this,
        notificationId,
        actionIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val title = downloadDetails.title
    val desc = downloadDetails.description
    val downloadStatus = downloadDetails.status

    with(NotificationCompat.Builder(this, channelId)) {
        setSmallIcon(R.drawable.ic_assistant_black_24dp)
        setContentTitle(if (downloadStatus == DownloadStatus.SUCCESS) {
            getString(R.string.download_success, title)
        } else {
            getString(R.string.download_failure, title)
        })
        setContentText(desc)
        addAction(R.drawable.ic_baseline_info_24, actionText, pendingIntent)
        priority = NotificationCompat.PRIORITY_HIGH

        notificationManager.notify(notificationId, build())
    }
}

fun Context.cancelNotification(notificationId: Int) {
    val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(notificationId)
}
