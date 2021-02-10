/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.domain

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class FollowFeed(
        val ownerId: String = "",
        val userId: String = "",
        val type: FeedType = FeedType.FOLLOW,
        @get:Exclude
        var authorUsername: String = "",
        @get:Exclude
        var authorProfilePictureUrl: String = "",
        val timestamp: Long = 0L
)