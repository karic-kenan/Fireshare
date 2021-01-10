package io.aethibo.fireshare.features.profile.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.EventObserver
import io.aethibo.fireshare.core.utils.FirebaseUtil.auth
import io.aethibo.fireshare.databinding.FragmentProfileBinding
import io.aethibo.fireshare.features.profile.viewmodel.ProfileViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class OthersProfileFragment : ProfileFragment(), View.OnClickListener {

    private val viewModel: ProfileViewModel by viewModel()
    private val binding: FragmentProfileBinding by viewBinding()

    override val uid: String
        get() = ""

    private val currentUser: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.profileHeader.btnToggleFollow.setOnClickListener(this)
        binding.profileHeader.btnMessageProfile.setOnClickListener(this)
    }

    private fun subscribeToObservers() {
        viewModel.profileMeta.observe(viewLifecycleOwner, EventObserver {
            binding.profileHeader.llProfileButtons.isVisible = true
            binding.profileHeader.btnProfileSettings.isVisible = auth.currentUser?.uid == uid
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnToggleFollow -> println("Hello follow")
            R.id.btnMessageProfile -> println("Hello message")
        }
    }
}