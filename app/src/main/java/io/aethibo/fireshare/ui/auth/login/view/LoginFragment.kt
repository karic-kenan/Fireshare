package io.aethibo.fireshare.ui.auth.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.aethibo.fireshare.MainActivity
import io.aethibo.fireshare.R
import io.aethibo.fireshare.framework.utils.EventObserver
import io.aethibo.fireshare.databinding.FragmentLoginBinding
import io.aethibo.fireshare.ui.auth.shared.AuthViewModel
import io.aethibo.fireshare.ui.utils.snackBar
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login), View.OnClickListener {

    private val binding: FragmentLoginBinding by viewBinding()
    private val viewModel: AuthViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
        setupButtonClickListeners()
    }

    private fun subscribeToObservers() {
        viewModel.loginStatus.observe(viewLifecycleOwner, EventObserver(
                onLoading = {
                    binding.signInProgressBar.isVisible = true
                },
                onSuccess = {
                    binding.signInProgressBar.isVisible = false
                    Intent(requireActivity(), MainActivity::class.java).also {
                        startActivity(it)
                        requireActivity().finish()
                    }
                },
                onError = {
                    binding.signInProgressBar.isVisible = false
                    snackBar(it)
                }
        ))
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