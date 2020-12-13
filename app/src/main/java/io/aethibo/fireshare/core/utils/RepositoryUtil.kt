package io.aethibo.fireshare.core.utils

import com.google.firebase.firestore.FirebaseFirestoreException

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: FirebaseFirestoreException) {
        when (e.code) {
            FirebaseFirestoreException.Code.NOT_FOUND -> {
                Resource.Error(e.localizedMessage)
            }
            else -> Resource.Error(e.localizedMessage)
        }
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage)
    }
}