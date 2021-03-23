/*
 * Created by Karic Kenan on 23.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.ActivityFeedItem
import io.aethibo.fireshare.framework.utils.Resource

interface GetNotificationFeedUseCase {
    suspend operator fun invoke(): Resource<List<ActivityFeedItem>>
}

class GetNotificationFeedUseCaseImpl(private val repository: MainRepository) : GetNotificationFeedUseCase {

    override suspend operator fun invoke(): Resource<List<ActivityFeedItem>> =
            repository.getNotificationFeed()
}