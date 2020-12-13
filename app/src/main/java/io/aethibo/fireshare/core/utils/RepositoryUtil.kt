package io.aethibo.fireshare.core.utils

import com.google.firebase.firestore.FirebaseFirestoreException

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: FirebaseFirestoreException) {
        when (e.code) {
            FirebaseFirestoreException.Code.NOT_FOUND -> {
                Resource.Failure(e)
            }
            else -> Resource.Failure(e)
        }
    } catch (e: Exception) {
        Resource.Failure(e)
    }
}