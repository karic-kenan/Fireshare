package io.aethibo.fireshare.domain.users

import io.aethibo.fireshare.core.data.repositories.main.MainRepository
import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.Resource

class UserUseCase(private val repository: MainRepository) : IUserUseCase {

    override suspend fun getUsers(uids: List<String>): Resource<List<User>> =
            repository.getUsers(uids)

    override suspend fun getSingleUser(uid: String): Resource<User> =
            repository.getSingleUser(uid)
}