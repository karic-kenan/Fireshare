/*
 * Created by Karic Kenan on 9.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.postdetail.viewmodel

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.Post
import timber.log.Timber

class DetailPostViewModel : ViewModel() {

    fun singlePostOptionsMenuClicked(
        context: Context,
        layoutInflater: LayoutInflater,
        post: Post,
        navigator: BottomNavigator
    ) {
        val builder = BottomSheetDialog(context)
        val dialogView = layoutInflater.inflate(R.layout.single_post_options_menu, null)
        val editButton = dialogView.findViewById<MaterialButton>(R.id.option_edit_button)
        val deleteButton = dialogView.findViewById<MaterialButton>(R.id.option_delete_button)
        val shareButton = dialogView.findViewById<MaterialButton>(R.id.option_share_button)

        builder.setContentView(dialogView)
        builder.show()

        deleteButton.setOnClickListener {
            Timber.i("Post ${post.id} deleted")
            // deletePost(context, post)
            builder.dismiss()
        }

        editButton.setOnClickListener {
            Timber.i("Post ${post.id} edited")
            // navigateToEditPost(post, navigator)
            builder.dismiss()
        }

        shareButton.setOnClickListener {
            Timber.i("Post ${post.id} shared")
            builder.dismiss()
        }
    }
}