/*
 * Created by Karic Kenan on 3.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.framework.datasource.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.framework.utils.AppConst
import kotlinx.coroutines.tasks.await

class ProfilePostsPagingSource(private val db: FirebaseFirestore, private val uid: String)
    : PagingSource<QuerySnapshot, Post>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {
            val currentPage = params.key
                    ?: db.collection(AppConst.postsCollection).document(uid).collection(AppConst.usersPostsCollection)
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

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Post>): QuerySnapshot? {
        TODO("Not yet implemented")
    }
}