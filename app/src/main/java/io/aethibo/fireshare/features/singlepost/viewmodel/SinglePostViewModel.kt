package io.aethibo.fireshare.features.singlepost.viewmodel

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.entities.Post
import timber.log.Timber

class SinglePostViewModel : ViewModel() {

    fun singlePostOptionsMenuClicked(
        context: Context,
        layoutInflater: LayoutInflater,
        post: Post
    ) {
        val builder = BottomSheetDialog(context)
        val dialogView = layoutInflater.inflate(R.layout.single_post_options_menu, null)
        val editButton = dialogView.findViewById<MaterialButton>(R.id.option_edit_button)
        val deleteButton = dialogView.findViewById<MaterialButton>(R.id.option_delete_button)
        val shareButton = dialogView.findViewById<MaterialButton>(R.id.option_share_button)
        val copyLinkButton = dialogView.findViewById<MaterialButton>(R.id.option_copy_link_button)

        builder.setContentView(dialogView)
        builder.show()

        deleteButton.setOnClickListener {
            Timber.i("Post ${post.id} deleted")
            builder.dismiss()
        }

        editButton.setOnClickListener {
            Timber.i("Post ${post.id} edited")
            builder.dismiss()
        }

        shareButton.setOnClickListener {
            Timber.i("Post ${post.id} shared")
            builder.dismiss()
        }

        copyLinkButton.setOnClickListener {
            Timber.i("Post ${post.id} copied")
            builder.dismiss()
        }
    }
}