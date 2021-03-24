package io.aethibo.fireshare.ui.auth.register.view

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
import io.aethibo.fireshare.databinding.FragmentRegisterBinding
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.auth.shared.AuthViewModel
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class RegisterFragment : Fragment(R.layout.fragment_register), View.OnClickListener {

    private val binding: FragmentRegisterBinding by viewBinding()
    private val viewModel: AuthViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
        setupButtonClickListeners()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.registerStatus.collectLatest { value: Resource<AuthResult> ->
                when (value) {
                    is Resource.Init -> Timber.d("Register screen")
                    is Resource.Loading -> binding.signUpProgressBar.isVisible = true
                    is Resource.Success -> {
                        binding.signUpProgressBar.isVisible = false
                        snackBar(getString(R.string.success_registration))

                        Intent(requireContext(), MainActivity::class.java).also {
                            startActivity(it)
                            requireActivity().finish()
                        }
                    }
                    is Resource.Failure -> {
                        binding.signUpProgressBar.isVisible = false
                        snackBar(value.message ?: "Unknown error occurred!")
                    }
                }
            }
        }
    }

    private fun setupButtonClickListeners() {
        binding.btnSignUp.setOnClickListener(this)
        binding.mtSignInLabel.setOnClickListener(this)
    }

    private fun signUp() = viewModel.register(
            binding.etEmail.text?.trim().toString(),
            binding.etUsername.text?.trim().toString(),
            binding.etPassword.text?.trim().toString(),
            binding.etRepeatPassword.text?.trim().toString()
    )

    private fun loginToAccount() {
        if (findNavController().previousBackStackEntry != null)
            findNavController().popBackStack()
        else
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSignUp -> signUp()
            R.id.mtSignInLabel -> loginToAccount()
        }
    }
}