package io.aethibo.fireshare.core.di

import io.aethibo.fireshare.domain.add.AddPostUseCase
import io.aethibo.fireshare.domain.add.IAddPostUseCase
import io.aethibo.fireshare.domain.auth.AuthUseCase
import io.aethibo.fireshare.domain.auth.IAuthUseCase
import org.koin.dsl.module

val useCasesModule = module {
    single<IAuthUseCase> { AuthUseCase(get()) }
    single<IAddPostUseCase> { AddPostUseCase(get()) }
}