package io.aethibo.fireshare.ui.auth.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.AuthResult
import io.aethibo.fireshare.MainActivity
import io.aethibo.fireshare.R
import io.aethibo.fireshare.databinding.FragmentLoginBinding
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.auth.shared.AuthViewModel
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class LoginFragment : Fragment(R.layout.fragment_login), View.OnClickListener {

    private val binding: FragmentLoginBinding by viewBinding()
    private val viewModel: AuthViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
        setupButtonClickListeners()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.loginStatus.collectLatest { value: Resource<AuthResult> ->
                when (value) {
                    is Resource.Init -> Timber.d("Login screen")
                    is Resource.Loading -> binding.signInProgressBar.isVisible = true
                    is Resource.Success -> {
                        binding.signInProgressBar.isVisible = false
                        Intent(requireActivity(), MainActivity::class.java).also {
                            startActivity(it)
                            requireActivity().finish()
                        }
                    }
                    is Resource.Failure -> {
                        binding.signInProgressBar.isVisible = false
                        snackBar(value.message ?: "Unknown error occurred!")
                    }
                }
            }
        }
    }

    private fun setupButtonClickListeners() {
        binding.btnSignIn.setOnClickListener(this)
        binding.mtSignUpLabel.setOnClickListener(this)
    }

    private fun signIn() =
            viewModel.login(binding.etEmail.text?.trim().toString(), binding.etPassword.text?.trim().toString())

    private fun registerAccount() {
        if (findNavController().previousBackStackEntry != null)
            findNavController().popBackStack()
        else
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSignIn -> signIn()
            R.id.mtSignUpLabel -> registerAccount()
        }
    }
}