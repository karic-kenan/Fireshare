/*
 * Created by Karic Kenan on 15.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.comments.viewmodel

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.Comment
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.usecases.CreateCommentUseCase
import io.aethibo.fireshare.usecases.DeleteCommentUseCase
import io.aethibo.fireshare.usecases.GetCommentsForPostUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentsViewModel(private val getComments: GetCommentsForPostUseCase,
                        private val createComment: CreateCommentUseCase,
                        private val deleteComment: DeleteCommentUseCase,
                        private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : ViewModel() {

    private val _commentsForPosts: MutableStateFlow<Resource<List<Comment>>> =
            MutableStateFlow(Resource.Loading())
    val commentsForPosts: StateFlow<Resource<List<Comment>>>
        get() = _commentsForPosts

    private val _createCommentStatus: MutableStateFlow<Resource<Comment>> = MutableStateFlow(Resource.Init())
    val createCommentStatus: StateFlow<Resource<Comment>>
        get() = _createCommentStatus

    private val _deleteCommentStatus: MutableStateFlow<Resource<Comment>> = MutableStateFlow(Resource.Init())
    val deleteCommentStatus: StateFlow<Resource<Comment>>
        get() = _deleteCommentStatus

    fun getCommentsForPost(postId: String) {
        viewModelScope.launch(dispatcher) {
            val result: Resource<List<Comment>> = getComments.invoke(postId)

            _commentsForPosts.value = result
        }
    }

    fun createComment(postId: String, commentText: String?) {

        if (commentText?.isEmpty()!!) return

        _createCommentStatus.value = Resource.Loading()

        viewModelScope.launch(dispatcher) {
            val result: Resource<Comment> = createComment.invoke(postId, commentText)

            _createCommentStatus.value = result
        }
    }

    fun deleteComment(comment: Comment) {
        _deleteCommentStatus.value = Resource.Loading()

        viewModelScope.launch(dispatcher) {
            val result: Resource<Comment> = deleteComment.invoke(comment)

            _deleteCommentStatus.value = result
        }
    }

    private fun openCommentDialog(navigator: BottomNavigator, comment: Comment) {
        // TODO: Add comment dialog if user should update comment?
    }

    fun showCommentContextMenu(
            context: Context,
            layoutInflater: LayoutInflater,
            navigator: BottomNavigator,
            comment: Comment
    ) {
        val builder = BottomSheetDialog(context)
        val dialogView = layoutInflater.inflate(R.layout.item_comment_options_menu, null)
        // val editButton = dialogView.findViewById<MaterialButton>(R.id.option_edit_comment_button)
        val deleteButton =
                dialogView.findViewById<MaterialButton>(R.id.option_delete_comment_button)

        builder.setContentView(dialogView)
        builder.show()

        /*editButton.setOnClickListener {
            Timber.d("Comment ${comment.id} edited")
            openCommentDialog(navigator, comment)
            builder.dismiss()
        }*/

        deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                    .setTitle(context.getText(R.string.delete_comment_dialog_title))
                    .setMessage(context.getText(R.string.delete_comment_dialog_subtitle))
                    .setNegativeButton(context.getText(R.string.actionCancel)) { _, _ -> }
                    .setPositiveButton(context.getText(R.string.actionDelete)) { _, _ ->
                        deleteComment(comment)
                    }
                    .show()
            builder.dismiss()
        }
    }
}