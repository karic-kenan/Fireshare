package io.aethibo.fireshare.core.data.pagingsource

import androidx.paging.PagingSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.aethibo.fireshare.core.entities.Post
import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.AppConst
import kotlinx.coroutines.tasks.await

class ProfilePostsPagingSource(
        private val db: FirebaseFirestore,
        private val uid: String
) : PagingSource<QuerySnapshot, Post>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {
            val currentPage = params.key ?: db.collection(AppConst.postsCollection).document(uid).collection(AppConst.usersPostsCollection)
                    .whereEqualTo("ownerId", uid)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = db.collection(AppConst.postsCollection).document(uid).collection(AppConst.usersPostsCollection)
                    .whereEqualTo("ownerId", uid)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastDocumentSnapshot)
                    .get()
                    .await()

            LoadResult.Page(
                    currentPage.toObjects(Post::class.java).onEach { post ->
                        val user = db.collection(AppConst.usersCollection)
                                .document(uid)
                                .get()
                                .await()
                                .toObject(User::class.java)!!

                        post.authorProfilePictureUrl = user.photoUrl
                        post.authorUsername = user.username
                    },
                    null,
                    nextPage
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}