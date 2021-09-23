package com.udacity.loadapp.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DownloadDetails(
    var title: String? = null,
    var description: String? = null,
    var status: DownloadStatus = DownloadStatus.FAILURE
): Parcelable
