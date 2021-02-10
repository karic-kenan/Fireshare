/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import com.google.firebase.auth.AuthResult
import io.aethibo.fireshare.data.remote.auth.AuthRepository
import io.aethibo.fireshare.domain.request.LoginRequestBody
import io.aethibo.fireshare.framework.utils.Resource

interface LoginUserUseCase {
    suspend operator fun invoke(body: LoginRequestBody): Resource<AuthResult>
}

class LoginUserUseCaseImpl(private val authRepository: AuthRepository) : LoginUserUseCase {

    override suspend fun invoke(body: LoginRequestBody) = authRepository.login(body)
}