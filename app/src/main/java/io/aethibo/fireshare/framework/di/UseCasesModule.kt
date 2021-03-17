/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.framework.di

import io.aethibo.fireshare.usecases.*
import org.koin.dsl.module

val useCasesModule = module {

    /**
     * Authentication
     */
    single<LoginUserUseCase> { LoginUserUseCaseImpl(get()) }
    single<RegisterUserUseCase> { RegisterUserUseCaseImpl(get()) }

    /**
     * Main - Post handler
     */
    single<CreatePostUseCase> { CreatePostUseCaseImpl(get()) }
    single<UpdatePostUseCase> { UpdatePostUseCaseImpl(get()) }
    single<DeletePostUseCase> { DeletePostUseCaseImpl(get()) }
    single<LikePostUseCase> { LikePostUseCaseImpl(get()) }
    single<GetTimelineUseCase> { GetTimelineUseCaseImpl(get()) }

    /**
     * Main - Users handler
     */
    single<GetSingleUserUseCase> { GetSingleUserUseCaseImpl(get()) }
    single<UpdateUserProfileUseCase> { UpdateUserProfileUseCaseImpl(get()) }
    single<SearchUserUseCase> { SearchUserUseCaseImpl(get()) }
    single<FollowUserUseCase> { FollowUserUseCaseImpl(get()) }
    single<CheckIsFollowingUseCase> { CheckIsFollowingUseCaseImpl(get()) }

    /**
     * Main - Comments handler
     */
    single<GetCommentsForPostUseCase> { GetCommentsForPostUseCaseImpl(get()) }
    single<CreateCommentUseCase> { CreateCommentUseCaseImpl(get()) }
    single<DeleteCommentUseCase> { DeleteCommentUseCaseImpl(get()) }
}