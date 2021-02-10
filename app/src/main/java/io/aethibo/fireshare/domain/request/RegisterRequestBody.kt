/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.domain.request

data class RegisterRequestBody(
        val username: String,
        val email: String,
        val password: String
)