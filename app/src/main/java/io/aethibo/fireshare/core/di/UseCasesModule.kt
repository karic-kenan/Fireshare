package io.aethibo.fireshare.core.di

import io.aethibo.fireshare.domain.add.AddPostUseCase
import io.aethibo.fireshare.domain.add.IAddPostUseCase
import io.aethibo.fireshare.domain.auth.AuthUseCase
import io.aethibo.fireshare.domain.auth.IAuthUseCase
import io.aethibo.fireshare.domain.users.IUserUseCase
import io.aethibo.fireshare.domain.users.UserUseCase
import org.koin.dsl.module

val useCasesModule = module {
    single<IAuthUseCase> { AuthUseCase(get()) }
    single<IAddPostUseCase> { AddPostUseCase(get()) }
    single<IUserUseCase> { UserUseCase(get()) }
}