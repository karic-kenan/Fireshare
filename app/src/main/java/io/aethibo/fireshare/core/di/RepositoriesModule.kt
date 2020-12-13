package io.aethibo.fireshare.core.di

import io.aethibo.fireshare.core.data.repositories.auth.AuthRepository
import io.aethibo.fireshare.core.data.repositories.auth.DefaultAuthRepository
import io.aethibo.fireshare.core.data.repositories.main.DefaultMainRepository
import io.aethibo.fireshare.core.data.repositories.main.MainRepository
import org.koin.dsl.module

val repositoriesModule = module {
    single<AuthRepository> { DefaultAuthRepository() }
    single<MainRepository> { DefaultMainRepository() }
}