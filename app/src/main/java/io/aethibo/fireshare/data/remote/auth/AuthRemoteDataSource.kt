/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.data.remote.auth

import com.google.firebase.auth.AuthResult
import io.aethibo.fireshare.domain.request.LoginRequestBody
import io.aethibo.fireshare.domain.request.RegisterRequestBody
import io.aethibo.fireshare.framework.utils.Resource

interface AuthRemoteDataSource {
    suspend fun login(body: LoginRequestBody): Resource<AuthResult>
    suspend fun register(body: RegisterRequestBody): Resource<AuthResult>
}