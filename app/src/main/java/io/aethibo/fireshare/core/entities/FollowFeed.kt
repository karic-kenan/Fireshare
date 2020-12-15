package io.aethibo.fireshare.core.entities

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