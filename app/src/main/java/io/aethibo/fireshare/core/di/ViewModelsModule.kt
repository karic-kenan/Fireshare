package io.aethibo.fireshare.core.di

import io.aethibo.fireshare.features.auth.shared.AuthViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { AuthViewModel(get(), get()) }
}