package io.aethibo.fireshare.features.addpost.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.utils.Event
import io.aethibo.fireshare.core.utils.Resource
import io.aethibo.fireshare.domain.add.IAddPostUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddPostViewModel(
        private val useCase: IAddPostUseCase,
        private val applicationContext: Context,
        private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _createPostStatus: MutableLiveData<Event<Resource<Any>>> = MutableLiveData()
    val createPostStatus: LiveData<Event<Resource<Any>>>
        get() = _createPostStatus

    private val _currentImageUri: MutableLiveData<Uri> = MutableLiveData()
    val currentImageUrl: LiveData<Uri>
        get() = _currentImageUri

    fun setCurrentImageUri(uri: Uri) {
        _currentImageUri.postValue(uri)
    }

    fun createPost(imageUri: Uri, text: String) {
        if (text.isEmpty()) {
            val error = applicationContext.getString(R.string.error_input_empty)
            _createPostStatus.postValue(Event(Resource.Error(error)))
        } else {
            _createPostStatus.postValue(Event(Resource.Loading()))
            viewModelScope.launch(dispatcher) {
                val result = useCase.createPost(imageUri, text)
                _createPostStatus.postValue(Event(result))
            }
        }
    }
}