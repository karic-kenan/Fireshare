/*
 * Created by Karic Kenan on 2.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.domain.request

import android.net.Uri

data class PostRequestBody(val caption: String, val imageUri: Uri)
