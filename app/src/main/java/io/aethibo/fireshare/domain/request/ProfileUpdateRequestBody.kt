/*
 * Created by Karic Kenan on 8.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.domain.request

import android.net.Uri

data class ProfileUpdateRequestBody(
        val uidToUpdate: String = "",
        val username: String = "",
        val bio: String = "",
        val location: String = "",
        val photoUrl: Uri? = null
)
