package io.aethibo.fireshare.features.utils

import androidx.fragment.app.Fragment
import io.aethibo.fireshare.features.shared.PostAdapter

abstract class BasePostFragment(layoutId: Int) : Fragment(layoutId) {

    val postsAdapter: PostAdapter by lazy { PostAdapter() }
}