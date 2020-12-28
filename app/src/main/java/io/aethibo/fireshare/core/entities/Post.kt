package io.aethibo.fireshare.core.entities

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Post(
        val id: String = "",
        val ownerId: String = "",
        @get:Exclude
        var authorUsername: String = "",
        @get:Exclude
        var authorProfilePictureUrl: String = "",
        val caption: String = "",
        val likes: List<Boolean> = emptyList(),
        val location: String = "",
        val imageUrl: String = "",
        val timestamp: Long = 0L,
) : Parcelable