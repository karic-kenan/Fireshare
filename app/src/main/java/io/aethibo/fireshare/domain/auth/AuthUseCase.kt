package io.aethibo.fireshare.domain.auth

import com.google.firebase.auth.AuthResult
import io.aethibo.fireshare.core.data.repositories.auth.AuthRepository
import io.aethibo.fireshare.core.utils.Resource

class AuthUseCase(private val repository: AuthRepository) : IAuthUseCase {

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): Resource<AuthResult> = repository.register(email, username, password)

    override suspend fun login(email: String, password: String): Resource<AuthResult> =
        repository.login(email, password)
}