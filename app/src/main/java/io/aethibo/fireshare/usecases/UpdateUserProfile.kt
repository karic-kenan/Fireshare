/*
 * Created by Karic Kenan on 8.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.request.ProfileUpdateRequestBody
import io.aethibo.fireshare.framework.utils.Resource

interface UpdateUserProfileUseCase {
    suspend operator fun invoke(body: ProfileUpdateRequestBody): Resource<Any>
}

class UpdateUserProfileUseCaseImpl(private val repository: MainRepository) : UpdateUserProfileUseCase {

    override suspend fun invoke(body: ProfileUpdateRequestBody): Resource<Any> =
            repository.updateUserProfile(body)
}