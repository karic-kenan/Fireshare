package io.aethibo.fireshare.core.entities

import android.net.Uri

data class ProfileUpdate(
        val uidToUpdate: String = "",
        val displayName: String = "",
        val username: String = "",
        val bio: String = "",
        val photoUrl: Uri? = null
)
