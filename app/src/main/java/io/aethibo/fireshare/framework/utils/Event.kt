/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.framework.utils

import androidx.lifecycle.Observer

class Event<out T>(private val content: T) {

    var hasBeenHandled: Boolean = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else null
    }

    fun peekContent() = content
}

class EventObserver<T>(
    private inline val onError: ((String) -> Unit)? = null,
    private inline val onLoading: (() -> Unit)? = null,
    private inline val onSuccess: ((T) -> Unit)
) : Observer<Event<Resource<T>>> {

    override fun onChanged(t: Event<Resource<T>>?) {
        when (val content = t?.peekContent()) {
            is Resource.Success -> {
                content.data?.let(onSuccess)
            }
            is Resource.Failure -> {
                t.getContentIfNotHandled()?.let {
                    onError?.let { error -> error(it.message!!) }
                }
            }
            is Resource.Loading -> {
                onLoading?.let { loading -> loading() }
            }
        }
    }
}