package com.rebeccablum.diane

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.rebeccablum.diane.databinding.DialogNewPostBinding

class AddPostDialog() : DialogFragment() {

    companion object {
        const val TAG = "Add Post Dialog"

        fun newInstance(): AddPostDialog {
            return AddPostDialog()
        }
    }

    private lateinit var resultListener: PostResultListener
    private lateinit var editText: EditText
    private lateinit var binding: DialogNewPostBinding

    interface PostResultListener {
        fun onPostSaved(postText: String)
        fun onPostCancelled()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let {
            resultListener = context as PostResultListener
        } ?: throw IllegalStateException("Context can't be null")
    }


    // TODO RLB start here - need access to data binding to get edit text object
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        editText = binding.post_edit_text

        val title = "New memo"
        val builder = AlertDialog.Builder(activity)
        builder.setView(R.layout.dialog_new_post)
        builder.setTitle(title)
        builder.setMessage("What would you like to tell me?")
        builder.setPositiveButton("Save") { _, _ ->
            Log.d(TAG, "Save post at this point.")
            val postText = editText.text.toString()
            resultListener.onPostSaved(postText)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            resultListener.onPostCancelled()
            dialog?.dismiss()
        }

        return builder.create()
    }
}
