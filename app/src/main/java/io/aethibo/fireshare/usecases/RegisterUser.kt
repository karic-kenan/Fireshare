/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import com.google.firebase.auth.AuthResult
import io.aethibo.fireshare.data.remote.auth.AuthRepository
import io.aethibo.fireshare.domain.request.RegisterRequestBody
import io.aethibo.fireshare.framework.utils.Resource

interface RegisterUserUseCase {
    suspend operator fun invoke(body: RegisterRequestBody): Resource<AuthResult>
}

class RegisterUserUseCaseImpl(private val authRepository: AuthRepository) : RegisterUserUseCase {

    override suspend fun invoke(body: RegisterRequestBody) =
            authRepository.register(body)
}