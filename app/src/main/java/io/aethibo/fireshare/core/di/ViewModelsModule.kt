package io.aethibo.fireshare.core.di

import io.aethibo.fireshare.features.add.viewmodel.AddPostViewModel
import io.aethibo.fireshare.features.auth.shared.AuthViewModel
import io.aethibo.fireshare.features.profile.viewmodel.ProfileViewModel
import io.aethibo.fireshare.features.settings.viewmodel.SettingsViewModel
import io.aethibo.fireshare.features.singlepost.viewmodel.CommentsViewModel
import io.aethibo.fireshare.features.singlepost.viewmodel.DetailPostViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { AddPostViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { DetailPostViewModel(get()) }
    viewModel { CommentsViewModel(get()) }
}