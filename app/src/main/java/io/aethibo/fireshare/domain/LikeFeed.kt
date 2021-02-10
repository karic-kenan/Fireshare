/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.domain

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class LikeFeed(
        val postId: String = "",
        val userId: String = "",
        @get:Exclude
        var authorUsername: String = "",
        @get:Exclude
        var authorProfilePictureUrl: String = "",
        val imageUrl: String = "",
        val type: FeedType = FeedType.LIKE,
        val timestamp: Long = 0L
)