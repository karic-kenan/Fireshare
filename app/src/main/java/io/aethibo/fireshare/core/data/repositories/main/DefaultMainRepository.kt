package io.aethibo.fireshare.core.data.repositories.main

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import io.aethibo.fireshare.core.entities.*
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
    private val comments = firestore.collection(AppConst.commentsCollection)

    override suspend fun createPost(imageUri: Uri, text: String): Resource<Any> =
            withContext(Dispatchers.IO) {
                safeCall {
                    val uid = auth.uid!!
                    val postId = UUID.randomUUID().toString()
                    val imageUploadResult = storage.getReference(postId).putFile(imageUri).await()
                    val imageUrl = imageUploadResult?.metadata?.reference?.downloadUrl?.await().toString()

                    val post = Post(
                            id = postId,
                            ownerId = uid,
                            caption = text,
                            imageUrl = imageUrl,
                            timestamp = System.currentTimeMillis()
                    )

                    posts.document(uid).collection(AppConst.usersPostsCollection).document(postId).set(post).await()

                    Resource.Success(Any())
                }
            }


    override suspend fun getSingleUser(uid: String): Resource<User> = withContext(Dispatchers.IO) {
        safeCall {
            val user = users.document(uid).get().await().toObject(User::class.java)
                    ?: throw IllegalStateException()
            Resource.Success(user)
        }
    }

    override suspend fun updateProfile(profileUpdate: ProfileUpdate): Resource<Any> = withContext(Dispatchers.IO) {
        safeCall {
            val imageUrl = profileUpdate.photoUrl?.let { uri ->
                updateProfilePicture(profileUpdate.uidToUpdate, uri).toString()
            }

            val map = mutableMapOf(
                    "displayName" to profileUpdate.displayName,
                    "username" to profileUpdate.username,
                    "bio" to profileUpdate.bio
            )

            imageUrl?.let { url ->
                map["photoUrl"] = url
            }

            users.document(profileUpdate.uidToUpdate).update(map.toMap()).await()

            Resource.Success(Any())
        }
    }

    override suspend fun updateProfilePicture(uid: String, imageUri: Uri): Uri? = withContext(Dispatchers.IO) {
        val storageRef = storage.getReference(uid)
        val user = getSingleUser(uid).data!!

        if (user.photoUrl != AppConst.DEFAULT_PROFILE_PICTURE_URL)
            storage.getReferenceFromUrl(user.photoUrl).delete().await()

        storageRef.putFile(imageUri)
                .await()
                .metadata
                ?.reference
                ?.downloadUrl
                ?.await()
    }

    override suspend fun updatePost(postToUpdate: PostToUpdate): Resource<Any> = withContext(Dispatchers.IO) {
        safeCall {
            val uid = auth.uid!!

            val map = mutableMapOf("caption" to postToUpdate.caption)

            posts
                    .document(uid)
                    .collection(AppConst.usersPostsCollection)
                    .document(postToUpdate.postIdToUpdate)
                    .update(map.toMap())
                    .await()

            Resource.Success(Any())
        }
    }

    override suspend fun deletePost(post: Post): Resource<Post> = withContext(Dispatchers.IO) {
        safeCall {
            val uid = auth.uid!!
            posts.document(uid).collection(AppConst.usersPostsCollection).document(post.id).delete().await()
            storage.getReferenceFromUrl(post.imageUrl).delete().await()
            Resource.Success(post)
        }
    }

    override suspend fun toggleLikeForPost(post: Post): Resource<Boolean> = withContext(Dispatchers.IO) {
        safeCall {

            var isLiked = false

            firestore.runTransaction { transaction ->
                val uid = auth.uid!!
                val postResult = transaction.get(posts.document(uid).collection(AppConst.usersPostsCollection).document(post.id))
                val currentLikes = postResult.toObject(Post::class.java)?.likedBy ?: emptyList()

                transaction.update(
                        posts.document(uid).collection(AppConst.usersPostsCollection).document(post.id),
                        "likedBy",
                        if (uid in currentLikes)
                            currentLikes - uid
                        else {
                            isLiked = true
                            currentLikes + uid
                        })
            }.await()

            Resource.Success(isLiked)
        }
    }

    override suspend fun getCommentsForPost(postId: String): Resource<List<Comment>> = withContext(Dispatchers.IO) {
        safeCall {

            val commentsForPost = comments
                    .document(postId)
                    .collection(AppConst.postCommentsCollection)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(Comment::class.java)
                    .onEach { comment ->
                        val user = getSingleUser(comment.userId).data!!

                        comment.authorUsername = user.username
                        comment.authorProfilePictureUrl = user.photoUrl
                    }

            Resource.Success(commentsForPost)
        }
    }

    override suspend fun createComment(postId: String, commentText: String): Resource<Comment> = withContext(Dispatchers.IO) {
        safeCall {
            val uid = auth.uid!!
            val user = getSingleUser(uid).data!!
            val commentId = UUID.randomUUID().toString()

            val comment = Comment(
                    id = commentId,
                    userId = uid,
                    postId = postId,
                    comment = commentText,
                    authorUsername = user.username,
                    authorProfilePictureUrl = user.photoUrl)

            comments.document(postId).collection(AppConst.postCommentsCollection).document(commentId).set(comment).await()

            Resource.Success(comment)
        }
    }

    override suspend fun deleteComment(comment: Comment): Resource<Comment> = withContext(Dispatchers.IO) {
        safeCall {
            comments
                    .document(comment.postId)
                    .collection(AppConst.postCommentsCollection)
                    .document(comment.id)
                    .delete()
                    .await()

            Resource.Success(comment)
        }
    }
}