package io.aethibo.fireshare.core.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class CommentFeed(
        val postId: String = "",
        val userId: String = "",
        val type: FeedType = FeedType.COMMENT,
        @get:Exclude
        var authorUsername: String = "",
        @get:Exclude
        var authorProfilePictureUrl: String = "",
        val imageUrl: String = "",
        val comment: String = "",
        val timestamp: Long = 0L
)