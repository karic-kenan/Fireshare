package io.aethibo.fireshare.domain.profile

import io.aethibo.fireshare.core.entities.ProfileUpdate
import io.aethibo.fireshare.core.utils.Resource

interface IProfileUseCase {
    suspend fun updateProfile(profileUpdate: ProfileUpdate): Resource<Any>
}