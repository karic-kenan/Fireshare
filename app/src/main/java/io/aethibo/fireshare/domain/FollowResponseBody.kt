/*
 * Created by Karic Kenan on 22.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowResponseBody(
        val userId: String? = "",
        val isFollowing: Boolean? = false
) : Parcelable
