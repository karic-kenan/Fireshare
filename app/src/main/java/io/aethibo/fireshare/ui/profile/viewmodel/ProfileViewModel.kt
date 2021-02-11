/*
 * Created by Karic Kenan on 3.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.profile.viewmodel

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
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
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.framework.datasource.main.ProfilePostsPagingSource
import io.aethibo.fireshare.framework.utils.AppConst
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.usecases.GetSingleUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel(
    private val getSingleUser: GetSingleUserUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _profileMeta: MutableStateFlow<Resource<User>> = MutableStateFlow(Resource.Init())
    val profileMeta: StateFlow<Resource<User>>
        get() = _profileMeta

    fun getPagingFlow(uid: String): Flow<PagingData<Post>> {
        val pagingSource = ProfilePostsPagingSource(FirebaseFirestore.getInstance(), uid)

        return Pager(PagingConfig(AppConst.PAGE_SIZE)) { pagingSource }.flow.cachedIn(viewModelScope)
    }

    fun loadProfile(uid: String) {
        _profileMeta.value = Resource.Loading()

        viewModelScope.launch(dispatcher) {
            val result: Resource<User> = getSingleUser.invoke(uid)

            _profileMeta.value = result
        }
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