package io.aethibo.fireshare.core.data.repositories.auth

import com.google.firebase.auth.AuthResult
import io.aethibo.fireshare.core.utils.Resource

interface AuthRepository {
    suspend fun register(email: String, username: String, password: String): Resource<AuthResult>

    suspend fun login(email: String, password: String): Resource<AuthResult>
}