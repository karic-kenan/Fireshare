package io.aethibo.fireshare.features.singlepost.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.aethibo.fireshare.core.entities.Comment
import io.aethibo.fireshare.core.utils.Event
import io.aethibo.fireshare.core.utils.Resource
import io.aethibo.fireshare.domain.comment.ICommentUseCase
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

    fun deleteComment(comment: Comment) {
        _deleteCommentStatus.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result = commentsUseCase.deleteComment(comment)

            _deleteCommentStatus.postValue(Event(result))
        }
    }
}