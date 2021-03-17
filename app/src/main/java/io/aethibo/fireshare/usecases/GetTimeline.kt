/*
 * Created by Karic Kenan on 17.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.framework.utils.Resource

interface GetTimelineUseCase {
    suspend operator fun invoke(): Resource<List<Post>>
}

class GetTimelineUseCaseImpl(private val repository: MainRepository) : GetTimelineUseCase {
    override suspend operator fun invoke() =
        repository.getTimeline()
}