package io.aethibo.fireshare.features.comment.viewmodel

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.entities.Comment
import io.aethibo.fireshare.core.entities.CommentToUpdate
import io.aethibo.fireshare.core.utils.Event
import io.aethibo.fireshare.core.utils.Resource
import io.aethibo.fireshare.domain.comment.ICommentUseCase
import io.aethibo.fireshare.features.comment.view.CommentsFragmentDirections
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentsViewModel(private val commentsUseCase: ICommentUseCase, private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : ViewModel() {

    private val _commentsForPosts: MutableLiveData<Event<Resource<List<Comment>>>> =
            MutableLiveData()
    val commentsForPosts: LiveData<Event<Resource<List<Comment>>>>
        get() = _commentsForPosts

    private val _createCommentStatus: MutableLiveData<Event<Resource<Comment>>> = MutableLiveData()
    val createCommentStatus: LiveData<Event<Resource<Comment>>>
        get() = _createCommentStatus

    private val _deleteCommentStatus: MutableLiveData<Event<Resource<Comment>>> = MutableLiveData()
    val deleteCommentStatus: LiveData<Event<Resource<Comment>>>
        get() = _deleteCommentStatus

    private val _updateCommentStatus: MutableLiveData<Event<Resource<Any>>> = MutableLiveData()
    val updateCommentStatus: LiveData<Event<Resource<Any>>>
        get() = _updateCommentStatus

    fun getCommentsForPost(postId: String) {
        _commentsForPosts.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result = commentsUseCase.getCommentsForPost(postId)

            _commentsForPosts.postValue(Event(result))
        }
    }

    fun createComment(postId: String, commentText: String?) {

        if (commentText?.isEmpty()!!) return

        _createCommentStatus.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result = commentsUseCase.createComment(postId, commentText)
            _createCommentStatus.postValue(Event(result))
        }
    }

    private fun openCommentDialog(navController: NavController, comment: Comment) =
            navController.navigate(CommentsFragmentDirections.globalActionToCommentDialog(comment))

    private fun deleteComment(comment: Comment) {
        _deleteCommentStatus.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result = commentsUseCase.deleteComment(comment)

            _deleteCommentStatus.postValue(Event(result))
        }
    }

    fun updateComment(commentToUpdate: CommentToUpdate) {
        _updateCommentStatus.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result = commentsUseCase.updateComment(commentToUpdate)

            _updateCommentStatus.postValue(Event(result))
        }
    }

    fun showCommentMenu(context: Context, layoutInflater: LayoutInflater, navController: NavController, comment: Comment) {
        val builder = BottomSheetDialog(context)
        val dialogView = layoutInflater.inflate(R.layout.item_comment_options_menu, null)
        val editButton = dialogView.findViewById<MaterialButton>(R.id.option_edit_comment_button)
        val deleteButton = dialogView.findViewById<MaterialButton>(R.id.option_delete_comment_button)

        builder.setContentView(dialogView)
        builder.show()

        editButton.setOnClickListener {
            openCommentDialog(navController, comment)
            builder.dismiss()
        }

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