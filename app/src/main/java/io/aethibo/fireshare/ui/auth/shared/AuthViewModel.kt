package io.aethibo.fireshare.ui.auth.shared

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import io.aethibo.fireshare.FireshareApp
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.request.LoginRequestBody
import io.aethibo.fireshare.domain.request.RegisterRequestBody
import io.aethibo.fireshare.framework.utils.AppConst
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.usecases.LoginUserUseCase
import io.aethibo.fireshare.usecases.RegisterUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
        private val loginUser: LoginUserUseCase,
        private val registerUser: RegisterUserUseCase,
        private val dispatchers: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _registerStatus: MutableStateFlow<Resource<AuthResult>> = MutableStateFlow(Resource.Init())
    val registerStatus: StateFlow<Resource<AuthResult>>
        get() = _registerStatus

    private val _loginStatus: MutableStateFlow<Resource<AuthResult>> = MutableStateFlow(Resource.Init())
    val loginStatus: StateFlow<Resource<AuthResult>>
        get() = _loginStatus

    fun register(email: String, username: String, password: String, repeatedPassword: String) {
        val error = when {
            email.isEmpty() or username.isEmpty() or password.isEmpty() -> {
                FireshareApp.instance.getString(R.string.error_input_empty)
            }
            password != repeatedPassword -> {
                FireshareApp.instance.getString(R.string.error_incorrectly_repeated_password)
            }
            username.length < AppConst.MIN_USERNAME_LENGTH -> {
                FireshareApp.instance.getString(
                        R.string.error_username_too_short,
                        AppConst.MIN_USERNAME_LENGTH
                )
            }
            username.length > AppConst.MAX_USERNAME_LENGTH -> {
                FireshareApp.instance.getString(
                        R.string.error_username_too_long,
                        AppConst.MAX_USERNAME_LENGTH
                )
            }
            password.length < AppConst.MIN_PASSWORD_LENGTH -> {
                FireshareApp.instance.getString(R.string.error_password_too_short)
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                FireshareApp.instance.getString(R.string.error_not_a_valid_email)
            }
            else -> null
        }

        error?.let {
            _registerStatus.value = Resource.Failure(it)
            return
        }

        _registerStatus.value = Resource.Loading()

        viewModelScope.launch(dispatchers) {
            val registerRequestBody = RegisterRequestBody(username, email, password)
            val result = registerUser.invoke(registerRequestBody)

            _registerStatus.value = result
        }
    }

    fun login(email: String, password: String) {

        if (email.isEmpty() or password.isEmpty()) {
            val error = FireshareApp.instance.getString(R.string.error_input_empty)
            _loginStatus.value = Resource.Failure(error)
        } else {
            _loginStatus.value = Resource.Loading()

            viewModelScope.launch(dispatchers) {
                val loginRequestBody = LoginRequestBody(email, password)
                val result = loginUser.invoke(loginRequestBody)

                _loginStatus.value = result
            }
        }
    }
}