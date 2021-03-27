package io.aethibo.fireshare.framework.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object FirebaseUtil {
    val auth = FirebaseAuth.getInstance()
    val storage = Firebase.storage
}