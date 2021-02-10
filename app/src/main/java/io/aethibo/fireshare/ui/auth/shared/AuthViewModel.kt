package io.aethibo.fireshare.ui.auth.shared

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.request.LoginRequestBody
import io.aethibo.fireshare.domain.request.RegisterRequestBody
import io.aethibo.fireshare.framework.utils.AppConst
import io.aethibo.fireshare.framework.utils.Event
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.usecases.LoginUserUseCase
import io.aethibo.fireshare.usecases.RegisterUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(
        private val loginUser: LoginUserUseCase,
        private val registerUser: RegisterUserUseCase,
        private val applicationContext: Context,
        private val dispatchers: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _registerStatus: MutableLiveData<Event<Resource<AuthResult>>> = MutableLiveData()
    val registerStatus: LiveData<Event<Resource<AuthResult>>>
        get() = _registerStatus

    private val _loginStatus: MutableLiveData<Event<Resource<AuthResult>>> = MutableLiveData()
    val loginStatus: LiveData<Event<Resource<AuthResult>>>
        get() = _loginStatus

    fun register(email: String, username: String, password: String, repeatedPassword: String) {
        val error = when {
            email.isEmpty() or username.isEmpty() or password.isEmpty() -> {
                applicationContext.getString(R.string.error_input_empty)
            }
            password != repeatedPassword -> {
                applicationContext.getString(R.string.error_incorrectly_repeated_password)
            }
            username.length < AppConst.MIN_USERNAME_LENGTH -> {
                applicationContext.getString(
                        R.string.error_username_too_short,
                        AppConst.MIN_USERNAME_LENGTH
                )
            }
            username.length > AppConst.MAX_USERNAME_LENGTH -> {
                applicationContext.getString(
                        R.string.error_username_too_long,
                        AppConst.MAX_USERNAME_LENGTH
                )
            }
            password.length < AppConst.MIN_PASSWORD_LENGTH -> {
                applicationContext.getString(R.string.error_password_too_short)
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                applicationContext.getString(R.string.error_not_a_valid_email)
            }
            else -> null
        }

        error?.let {
            _registerStatus.postValue(Event(Resource.Failure(it)))
            return
        }

        _registerStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatchers) {
            val registerRequestBody = RegisterRequestBody(username, email, password)
            val result = registerUser.invoke(registerRequestBody)
            _registerStatus.postValue(Event(result))
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            val error = applicationContext.getString(R.string.error_input_empty)
            _loginStatus.postValue(Event(Resource.Failure(error)))
        } else {
            _loginStatus.postValue(Event(Resource.Loading()))
            viewModelScope.launch(dispatchers) {
                val loginRequestBody = LoginRequestBody(email, password)
                val result = loginUser.invoke(loginRequestBody)
                _loginStatus.postValue(Event(result))
            }
        }
    }
}