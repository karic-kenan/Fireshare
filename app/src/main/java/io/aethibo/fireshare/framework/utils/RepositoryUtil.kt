/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.framework.utils

import com.google.firebase.firestore.FirebaseFirestoreException

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: FirebaseFirestoreException) {
        when (e.code) {
            FirebaseFirestoreException.Code.NOT_FOUND -> {
                Resource.Failure(e.localizedMessage)
            }
            else -> Resource.Failure(e.localizedMessage)
        }
    } catch (e: Exception) {
        Resource.Failure(e.localizedMessage)
    }
}