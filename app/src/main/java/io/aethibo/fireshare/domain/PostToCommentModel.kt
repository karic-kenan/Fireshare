/*
 * Created by Karic Kenan on 23.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostToCommentModel(val postId: String, val postImage: String, val ownerId: String) : Parcelable