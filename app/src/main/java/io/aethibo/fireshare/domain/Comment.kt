package io.aethibo.fireshare.domain

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import java.util.*

@IgnoreExtraProperties
@Parcelize
data class Comment(
        val id: String = UUID.randomUUID().toString(),
        val userId: String = "",
        val postId: String = "",
        val comment: String = "",
        val timestamp: Long = System.currentTimeMillis(),
        @get:Exclude
        var authorUsername: String = "",
        @get:Exclude
        var authorProfilePictureUrl: String = "",
) : Parcelable