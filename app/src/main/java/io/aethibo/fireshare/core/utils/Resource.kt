package io.aethibo.fireshare.core.utils

sealed class Resource<out T> {
    class Loading<out T> : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(val message: String?, val data: T? = null) : Resource<T>()
}