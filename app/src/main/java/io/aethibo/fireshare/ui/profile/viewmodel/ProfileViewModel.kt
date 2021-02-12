/*
 * Created by Karic Kenan on 3.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.profile.viewmodel

import android.content.Context
import android.view.LayoutInflater
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
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.framework.datasource.main.ProfilePostsPagingSource
import io.aethibo.fireshare.framework.utils.AppConst
import io.aethibo.fireshare.ui.base.BasePostViewModel
import io.aethibo.fireshare.usecases.GetSingleUserUseCase
import io.aethibo.fireshare.usecases.LikePostUseCase
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class ProfileViewModel(
        getSingleUser: GetSingleUserUseCase,
        likePostUseCase: LikePostUseCase
) : BasePostViewModel(getSingleUser, likePostUseCase) {

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
            // deletePost(context, post)
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

    fun logout() {
        // Log out user
        // Navigate back to auth fragment
    }
}