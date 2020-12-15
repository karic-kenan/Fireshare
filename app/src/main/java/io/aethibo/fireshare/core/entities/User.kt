package io.aethibo.fireshare.core.entities

import io.aethibo.fireshare.core.utils.AppConst
import java.util.*

data class User(
        val uid: String = UUID.randomUUID().toString(),
        val username: String = "",
        val bio: String = "",
        val photoUrl: String = AppConst.DEFAULT_PROFILE_IMAGE_URL,
)