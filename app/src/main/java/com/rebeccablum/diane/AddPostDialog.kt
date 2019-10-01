package com.rebeccablum.diane

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.rebeccablum.diane.databinding.DialogAddPostBinding

class AddPostDialog : DialogFragment() {

    companion object {
        const val TAG = "Add Post Dialog"

        fun newInstance(): AddPostDialog {
            return AddPostDialog()
        }
    }

    private lateinit var resultListener: PostResultListener
    private lateinit var binding: DialogAddPostBinding

    interface PostResultListener {
        fun onPostSaved(postText: String)
        fun onPostCancelled()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        resultListener = context as PostResultListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.dialog_add_post,
            null,
            false
        )
        binding.addPostViewModel = (activity as HomeActivity).getAddPostViewModel()
        val title = "New memo"
        val builder = AlertDialog.Builder(activity)

        builder.setView(binding.root)
        builder.setTitle(title)
        builder.setMessage("What would you like to tell me?")
        builder.setPositiveButton("Save") { _, _ ->
            binding.addPostViewModel?.currentText?.get()?.let {
                resultListener.onPostSaved(it)
            } ?: run {
                Log.e(TAG, "Post text is null.")
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            resultListener.onPostCancelled()
            dialog?.dismiss()
        }

        return builder.create()
    }
}
