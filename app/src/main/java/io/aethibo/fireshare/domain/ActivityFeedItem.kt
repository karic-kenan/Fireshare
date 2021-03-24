/*
 * Created by Karic Kenan on 23.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActivityFeedItem(
        var username: String = "",
        val userId: String = "",
        val type: String = "",
        val imageUrl: String = "",
        val postId: String = "",
        var avatar: String = "",
        val comment: String = "",
        val timestamp: Long = 0L
) : Parcelable
