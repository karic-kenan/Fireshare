package io.aethibo.fireshare.domain.users

import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.Resource

interface IUserUseCase {
    suspend fun getUsers(uids: List<String>): Resource<List<User>>

    suspend fun getSingleUser(uid: String): Resource<User>
}