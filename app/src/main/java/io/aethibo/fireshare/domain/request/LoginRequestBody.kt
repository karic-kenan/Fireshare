/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.domain.request

data class LoginRequestBody(
        val email: String,
        val password: String
)