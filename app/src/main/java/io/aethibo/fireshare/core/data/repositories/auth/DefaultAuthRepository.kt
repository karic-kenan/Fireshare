package io.aethibo.fireshare.core.data.repositories.auth

import com.google.firebase.auth.AuthResult
import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.AppConst
import io.aethibo.fireshare.core.utils.FirebaseUtil.auth
import io.aethibo.fireshare.core.utils.FirebaseUtil.firestore
import io.aethibo.fireshare.core.utils.Resource
import io.aethibo.fireshare.core.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DefaultAuthRepository : AuthRepository {

    private val usersCollection =
        firestore.collection(AppConst.usersCollection)

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): Resource<AuthResult> = withContext(Dispatchers.IO) {
        safeCall {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid!!
            val user = User(uid, username)
            usersCollection.document(uid).set(user).await()
            Resource.Success(result)
        }
    }

    override suspend fun login(email: String, password: String): Resource<AuthResult> =
        withContext(Dispatchers.IO) {
            safeCall {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result)
            }
        }
}