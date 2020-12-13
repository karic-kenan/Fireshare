package io.aethibo.fireshare.core.data.repositories.main

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import io.aethibo.fireshare.core.entities.Post
import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.AppConst
import io.aethibo.fireshare.core.utils.Resource
import io.aethibo.fireshare.core.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class DefaultMainRepository : MainRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage
    private val posts = firestore.collection(AppConst.postsCollection)
    private val users = firestore.collection(AppConst.usersCollection)

    override suspend fun createPost(imageUri: Uri, text: String): Resource<Any> =
            withContext(Dispatchers.IO) {
                safeCall {
                    val uid = auth.uid!!
                    val postId = UUID.randomUUID().toString()
                    val imageUploadResult = storage.getReference(postId).putFile(imageUri).await()
                    val imageUrl =
                            imageUploadResult?.metadata?.reference?.downloadUrl?.await().toString()
                    val post = Post(
                            id = postId,
                            authorUid = uid,
                            text = text,
                            imageUrl = imageUrl,
                            date = System.currentTimeMillis()
                    )

                    posts.document(postId).set(post).await()

                    Resource.Success(Any())
                }
            }

    override suspend fun getUsers(uids: List<String>): Resource<List<User>> =
            withContext(Dispatchers.IO) {
                safeCall {
                    val chunks = uids.chunked(10)
                    val resultList = mutableListOf<User>()

                    chunks.forEach { chunk ->
                        val usersList = users.whereIn("uid", uids)
                                .orderBy("username")
                                .get()
                                .await()
                                .toObjects(User::class.java)

                        resultList.addAll(usersList)
                    }

                    Resource.Success(resultList.toList())
                }
            }

    override suspend fun getSingleUser(uid: String): Resource<User> = withContext(Dispatchers.IO) {
        safeCall {
            val user = users.document(uid).get().await().toObject(User::class.java)
                    ?: throw IllegalStateException()
            val currentUid = FirebaseAuth.getInstance().uid!!
            val currentUser = users.document(currentUid).get().await().toObject(User::class.java)
                    ?: throw IllegalStateException()
            user.isFollowing = uid in currentUser.follows
            Resource.Success(user)
        }
    }
}