package io.aethibo.fireshare.core.entities

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