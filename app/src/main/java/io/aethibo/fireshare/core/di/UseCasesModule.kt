package io.aethibo.fireshare.core.di

import io.aethibo.fireshare.domain.add.AddPostUseCase
import io.aethibo.fireshare.domain.add.IAddPostUseCase
import io.aethibo.fireshare.domain.auth.AuthUseCase
import io.aethibo.fireshare.domain.auth.IAuthUseCase
import io.aethibo.fireshare.domain.comment.CommentUseCase
import io.aethibo.fireshare.domain.comment.ICommentUseCase
import io.aethibo.fireshare.domain.post.IPostUseCase
import io.aethibo.fireshare.domain.post.PostUseCase
import io.aethibo.fireshare.domain.profile.IProfileUseCase
import io.aethibo.fireshare.domain.profile.ProfileUseCase
import io.aethibo.fireshare.domain.search.ISearchUseCase
import io.aethibo.fireshare.domain.search.SearchUseCase
import io.aethibo.fireshare.domain.users.IUserUseCase
import io.aethibo.fireshare.domain.users.UserUseCase
import org.koin.dsl.module

val useCasesModule = module {
    single<IAuthUseCase> { AuthUseCase(get()) }
    single<IAddPostUseCase> { AddPostUseCase(get()) }
    single<IUserUseCase> { UserUseCase(get()) }
    single<IProfileUseCase> { ProfileUseCase(get()) }
    single<IPostUseCase> { PostUseCase(get()) }
    single<ICommentUseCase> { CommentUseCase(get()) }
    single<ISearchUseCase> { SearchUseCase(get()) }
}