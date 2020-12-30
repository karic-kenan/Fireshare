package io.aethibo.fireshare.features.singlepost.viewmodel

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
import io.aethibo.fireshare.core.entities.Post
import io.aethibo.fireshare.core.entities.PostToUpdate
import io.aethibo.fireshare.core.utils.Event
import io.aethibo.fireshare.core.utils.Resource
import io.aethibo.fireshare.domain.post.IPostUseCase
import io.aethibo.fireshare.features.singlepost.view.DetailPostFragmentDirections
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailPostViewModel(private val postUseCase: IPostUseCase, private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : ViewModel() {

    private var likeJob: Job? = null
    private val _likePostStatus: MutableLiveData<Event<Resource<Boolean>>> = MutableLiveData()
    val likePostStatus: LiveData<Event<Resource<Boolean>>>
        get() = _likePostStatus

    private val _deletePostStatus: MutableLiveData<Event<Resource<Post>>> = MutableLiveData()
    val deletePostStatus: LiveData<Event<Resource<Post>>>
        get() = _deletePostStatus

    private var updatePostJob: Job? = null
    private val _updatePostStatus: MutableLiveData<Event<Resource<Any>>> = MutableLiveData()
    val updatePostStatus: LiveData<Event<Resource<Any>>>
        get() = _updatePostStatus

    fun toggleLikeForPost(post: Post) {
        likeJob?.cancel()

        _likePostStatus.postValue(Event(Resource.Loading()))
        likeJob = viewModelScope.launch(dispatcher) {
            val result = postUseCase.toggleLikeForPost(post)
            _likePostStatus.postValue(Event(result))
        }
    }

    fun updatePost(postToUpdate: PostToUpdate) {
        updatePostJob?.cancel()

        _updatePostStatus.postValue(Event(Resource.Loading()))
        updatePostJob = viewModelScope.launch(dispatcher) {
            val result = postUseCase.updatePost(postToUpdate)
            _updatePostStatus.postValue(Event(result))
        }
    }

    private fun deletePost(context: Context, post: Post) {
        AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.delete_post_dialog_title))
                .setMessage(context.getString(R.string.delete_post_dialog_subtitle))
                .setNegativeButton(context.getString(R.string.delete_post_dialog_negative_button)) { _, _ -> }
                .setPositiveButton(context.getString(R.string.delete_post_dialog_positive_button)) { _, _ ->
                    _deletePostStatus.postValue(Event(Resource.Loading()))
                    viewModelScope.launch(dispatcher) {
                        val result = postUseCase.deletePost(post)
                        _deletePostStatus.postValue(Event(result))
                    }
                }
                .show()
    }

    fun singlePostOptionsMenuClicked(
            context: Context,
            layoutInflater: LayoutInflater,
            post: Post,
            findNavController: NavController
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
            findNavController.navigate(DetailPostFragmentDirections.actionDetailPostFragmentToEditPostFragment(post))
            builder.dismiss()
        }

        shareButton.setOnClickListener {
            Timber.i("Post ${post.id} shared")
            builder.dismiss()
        }
    }
}