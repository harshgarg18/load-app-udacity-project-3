package com.udacity.loadapp.utils

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.udacity.loadapp.R


fun Context.downloadFile(
    url: String,
    title: String = getString(R.string.app_name),
    desc: String = getString(R.string.app_description)
): Long {
    val request =
        DownloadManager.Request(Uri.parse(url))
            .setTitle(title)
            .setDescription(desc)
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

    val downloadManager = getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
    return downloadManager.enqueue(request)
}

@SuppressLint("Range")
fun Context.checkDownloadStatus(id: Long): DownloadDetails {
    val downloadManager = getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
    val cursor = downloadManager.query(DownloadManager.Query().setFilterById(id))
    if (cursor.moveToFirst()) {
        val title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
        val desc = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))
        val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
        val downloadStatus = if (DownloadManager.STATUS_SUCCESSFUL == status) {
            DownloadStatus.SUCCESS
        } else {
            DownloadStatus.FAILURE
        }
        return DownloadDetails(title = title, description = desc, status = downloadStatus)
    }
    return DownloadDetails()
}
