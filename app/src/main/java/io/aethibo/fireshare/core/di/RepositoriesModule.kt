package io.aethibo.fireshare.core.di

import io.aethibo.fireshare.core.data.repositories.auth.AuthRepository
import io.aethibo.fireshare.core.data.repositories.auth.DefaultAuthRepository
import org.koin.dsl.module

val repositoriesModule = module {
    single<AuthRepository> { DefaultAuthRepository() }
}