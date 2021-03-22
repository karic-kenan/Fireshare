/*
 * Created by Karic Kenan on 3.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.profile.viewmodel

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.FollowResponseBody
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.framework.datasource.main.ProfilePostsPagingSource
import io.aethibo.fireshare.framework.utils.AppConst
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.base.BasePostViewModel
import io.aethibo.fireshare.usecases.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel(
        getSingleUser: GetSingleUserUseCase,
        likePostUseCase: LikePostUseCase,
        updatePost: UpdatePostUseCase,
        private val deletePost: DeletePostUseCase,
        private val followUser: FollowUserUseCase,
        private val isFollowing: CheckIsFollowingUseCase,
        private val postsCount: GetPostsCountUseCase,
        private val followingCount: GetFollowingCountUseCase,
        private val followersCount: GetFollowersCountUseCase,
        addLike: FeedAddLikeUseCase,
        removeLike: FeedRemoveLikeUseCase,
        addFollow: FeedAddFollowUseCase,
        removeFollow: FeedRemoveFollowUseCase
) : BasePostViewModel(getSingleUser, likePostUseCase, addLike, removeLike, addFollow, removeFollow) {

    private val _deletePostStatus: MutableStateFlow<Resource<Post>> = MutableStateFlow(Resource.Init())
    val deletePostStatus: StateFlow<Resource<Post>>
        get() = _deletePostStatus

    private val _followStatus: MutableStateFlow<Resource<FollowResponseBody>> = MutableStateFlow(Resource.Init())
    val followStatus: StateFlow<Resource<FollowResponseBody>>
        get() = _followStatus

    private val _isFollowingStatus: MutableStateFlow<Resource<FollowResponseBody>> = MutableStateFlow(Resource.Init())
    val isFollowingStatus: StateFlow<Resource<FollowResponseBody>>
        get() = _isFollowingStatus

    private val _postsCountStatus: MutableStateFlow<Resource<Int>> = MutableStateFlow(Resource.Loading())
    val postsCountStatus: StateFlow<Resource<Int>>
        get() = _postsCountStatus

    private val _followingCountStatus: MutableStateFlow<Resource<Int>> = MutableStateFlow(Resource.Loading())
    val followingCountStatus: StateFlow<Resource<Int>>
        get() = _followingCountStatus

    private val _followersCountStatus: MutableStateFlow<Resource<Int>> = MutableStateFlow(Resource.Loading())
    val followersCountStatus: StateFlow<Resource<Int>>
        get() = _followersCountStatus

    fun getPagingFlow(uid: String): Flow<PagingData<Post>> {
        val pagingSource = ProfilePostsPagingSource(FirebaseFirestore.getInstance(), uid)

        return Pager(PagingConfig(AppConst.PAGE_SIZE)) { pagingSource }.flow.cachedIn(viewModelScope)
    }

    fun singlePostOptionsMenuClicked(
            context: Context,
            layoutInflater: LayoutInflater,
            post: Post,
            navigator: BottomNavigator
    ) {
        val builder = BottomSheetDialog(context)
        val dialogView = layoutInflater.inflate(R.layout.single_post_options_menu, null)
        val editButton = dialogView.findViewById<MaterialButton>(R.id.option_edit_button)
        val deleteButton = dialogView.findViewById<MaterialButton>(R.id.option_delete_button)
        val shareButton = dialogView.findViewById<MaterialButton>(R.id.option_share_button)

        builder.setContentView(dialogView)
        builder.show()

        deleteButton.setOnClickListener {
            Timber.i("Post ${post.id} deleted")
            deletePost(context, post)
            builder.dismiss()
        }

        editButton.setOnClickListener {
            Timber.i("Post ${post.id} edited")
            // navigateToEditPost(post, navigator)
            builder.dismiss()
        }

        shareButton.setOnClickListener {
            Timber.i("Post ${post.id} shared")
            builder.dismiss()
        }
    }

    private fun deletePost(context: Context, post: Post) {
        AlertDialog.Builder(context)
                .setTitle("Delete post")
                .setMessage("Are you sure you want to delete this post?")
                .setNegativeButton("Cancel") { _, _ -> }
                .setPositiveButton("Delete") { _, _ ->
                    _deletePostStatus.value = Resource.Loading()

                    viewModelScope.launch(dispatcher) {
                        val result: Resource<Post> = deletePost.invoke(post)
                        _deletePostStatus.value = result
                    }
                }
                .show()
    }

    fun toggleFollowUser(uid: String) {
        _followStatus.value = Resource.Loading()

        viewModelScope.launch(dispatcher) {
            val result: Resource<FollowResponseBody> = followUser.invoke(uid)

            _followStatus.value = result
        }
    }

    fun checkIsFollowing(uid: String) {
        _isFollowingStatus.value = Resource.Loading()

        viewModelScope.launch(dispatcher) {
            val result: Resource<FollowResponseBody> = isFollowing.invoke(uid)

            _isFollowingStatus.value = result
        }
    }

    fun getPostsCount(uid: String) {
        viewModelScope.launch(dispatcher) {
            val result: Resource<Int> = postsCount.invoke(uid)

            _postsCountStatus.value = result
        }
    }

    fun getFollowingCount(uid: String) {
        viewModelScope.launch(dispatcher) {
            val result: Resource<Int> = followingCount.invoke(uid)

            _followingCountStatus.value = result
        }
    }

    fun getFollowersCount(uid: String) {
        viewModelScope.launch(dispatcher) {
            val result: Resource<Int> = followersCount.invoke(uid)

            _followersCountStatus.value = result
        }
    }

    fun logout() {
        // Log out user
        // Navigate back to auth fragment
    }
}