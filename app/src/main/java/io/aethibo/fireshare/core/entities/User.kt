package io.aethibo.fireshare.core.entities

import android.os.Parcelable
import io.aethibo.fireshare.core.utils.AppConst
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(
        val uid: String = UUID.randomUUID().toString(),
        val username: String = "",
        val displayName: String = "",
        val bio: String = "",
        val location: String = "",
        val photoUrl: String = AppConst.DEFAULT_PROFILE_PICTURE_URL,
) : Parcelable