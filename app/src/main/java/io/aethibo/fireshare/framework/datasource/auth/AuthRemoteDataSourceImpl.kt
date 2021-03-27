/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.framework.datasource.auth

import com.google.firebase.firestore.FirebaseFirestore
import io.aethibo.fireshare.data.remote.auth.AuthRemoteDataSource
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.domain.request.LoginRequestBody
import io.aethibo.fireshare.domain.request.RegisterRequestBody
import io.aethibo.fireshare.framework.utils.AppConst
import io.aethibo.fireshare.framework.utils.FirebaseUtil.auth
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.framework.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthenticationDataSourceImpl : AuthRemoteDataSource {

    private val usersCollection =
            FirebaseFirestore.getInstance().collection(AppConst.usersCollection)

    override suspend fun login(body: LoginRequestBody) = withContext(Dispatchers.IO) {
        safeCall {
            val result = auth.signInWithEmailAndPassword(body.email, body.password).await()
            Resource.Success(result)
        }
    }

    override suspend fun register(body: RegisterRequestBody) = withContext(Dispatchers.IO) {
        safeCall {
            val result = auth.createUserWithEmailAndPassword(body.email, body.password).await()
            val uid = result.user?.uid!!
            val user = User(uid, body.username, body.fullName)
            usersCollection.document(uid).set(user).await()
            Resource.Success(result)
        }
    }
}