/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.domain

import android.os.Parcelable
import io.aethibo.fireshare.framework.utils.AppConst
import kotlinx.parcelize.Parcelize
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