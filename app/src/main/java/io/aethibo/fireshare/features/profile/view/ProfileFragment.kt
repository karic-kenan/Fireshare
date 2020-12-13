package io.aethibo.fireshare.features.profile.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.utils.EventObserver
import io.aethibo.fireshare.databinding.FragmentProfileBinding
import io.aethibo.fireshare.features.profile.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()
    private val viewModel: ProfileViewModel by viewModel()

    private val uid: String
        get() = FirebaseAuth.getInstance().uid!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadProfile(uid)

        setupRecyclerView()
        setupButtonClickListeners()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.profileMeta.observe(viewLifecycleOwner, EventObserver(
                onLoading = {
                    profileProgressBar?.isVisible = true
                },
                onSuccess = { user ->
                    profileProgressBar?.isVisible = false
                    binding.username.text = user.username
                },
                onError = {
                    profileProgressBar?.isVisible = false
                }
        ))
    }

    private fun setupButtonClickListeners() {

    }

    private fun setupRecyclerView() {

    }
}