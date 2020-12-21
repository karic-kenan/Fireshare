package io.aethibo.fireshare.domain.profile

import io.aethibo.fireshare.core.data.repositories.main.MainRepository
import io.aethibo.fireshare.core.entities.ProfileUpdate
import io.aethibo.fireshare.core.utils.Resource

class ProfileUseCase(private val repository: MainRepository): IProfileUseCase {

    override suspend fun updateProfile(profileUpdate: ProfileUpdate): Resource<Any> =
            repository.updateProfile(profileUpdate)
}