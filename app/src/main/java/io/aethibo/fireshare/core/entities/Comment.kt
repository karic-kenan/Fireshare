package io.aethibo.fireshare.core.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Comment(
        val commentId: String = UUID.randomUUID().toString(),
        val userId: String = "",
        val comment: String = "",
        val timestamp: Long = 0L,
        @get:Exclude
        var authorUsername: String = "",
        @get:Exclude
        var authorProfilePictureUrl: String = "",
)