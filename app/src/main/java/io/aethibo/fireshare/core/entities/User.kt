package io.aethibo.fireshare.core.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val uid: String = "",
    val username: String = "",
    val profilePictureUrl: String = "DEFAULT_PROFILE_PICTURE_URL",
    val description: String = "",
    var follows: List<String> = emptyList(),
    @get:Exclude
    var isFollowing: Boolean = false
)