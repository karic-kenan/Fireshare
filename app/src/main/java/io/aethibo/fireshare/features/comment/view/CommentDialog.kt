package io.aethibo.fireshare.features.comment.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.entities.Comment
import io.aethibo.fireshare.core.entities.CommentToUpdate
import io.aethibo.fireshare.core.utils.EventObserver
import io.aethibo.fireshare.databinding.DialogFragmentCommentBinding
import io.aethibo.fireshare.features.comment.viewmodel.CommentsViewModel
import io.aethibo.fireshare.features.utils.snackBar
import org.koin.android.viewmodel.ext.android.viewModel

class CommentDialog : DialogFragment(), View.OnClickListener {

    private val viewModel: CommentsViewModel by viewModel()
    private val binding: DialogFragmentCommentBinding by viewBinding()

    private lateinit var dialogView: View
    private lateinit var comment: Comment

    companion object {
        fun newInstance(comment: Comment) = CommentDialog().apply {
            arguments = Bundle().apply {
                putParcelable("comment", comment)
            }
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dialogView =
            LayoutInflater
                .from(requireContext())
                .inflate(R.layout.dialog_fragment_comment, null)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit comment")
            .setView(dialogView)
            .create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getParcelable<Comment>("comment")?.let { comment ->
            this.comment = comment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = dialogView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupClickListeners()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.updateCommentStatus.observe(viewLifecycleOwner, EventObserver(
            onLoading = {
                binding.pbCommentDialog.isVisible = true
            },
            onSuccess = {
                binding.pbCommentDialog.isVisible = false
                dismiss()
            },
            onError = {
                binding.pbCommentDialog.isVisible = false
                snackBar(it)
            }
        ))
    }

    private fun setupClickListeners() {
        binding.mbCommentDialogUpdate.setOnClickListener(this)
    }

    private fun setupViews() {
        binding.etCommentDialog.setText(comment.comment)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.mbCommentDialogUpdate -> {
                val commentToUpdate = CommentToUpdate(
                    comment.id,
                    comment.postId,
                    binding.etCommentDialog.text?.trim().toString()
                )

                viewModel.updateComment(commentToUpdate)
            }
        }
    }
}