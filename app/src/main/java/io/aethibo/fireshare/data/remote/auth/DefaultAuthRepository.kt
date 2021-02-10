package io.aethibo.fireshare.data.remote.auth

import com.google.firebase.auth.AuthResult
import io.aethibo.fireshare.domain.request.LoginRequestBody
import io.aethibo.fireshare.domain.request.RegisterRequestBody
import io.aethibo.fireshare.framework.utils.Resource

class DefaultAuthRepository(private val authRemote: AuthRemoteDataSource) : AuthRepository {

    override suspend fun login(body: LoginRequestBody): Resource<AuthResult> = authRemote.login(body)

    override suspend fun register(body: RegisterRequestBody): Resource<AuthResult> =
            authRemote.register(body)
}