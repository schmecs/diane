package com.rebeccablum.diane

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.rebeccablum.diane.databinding.DialogAddPostBinding
import com.rebeccablum.diane.viewmodels.AddPostViewModel

class AddPostDialog : DialogFragment() {

    companion object {
        const val TAG = "Add Post Dialog"

        fun newInstance(): AddPostDialog {
            return AddPostDialog()
        }
    }

    private lateinit var resultListener: PostResultListener
    private lateinit var binding: DialogAddPostBinding
    private lateinit var textInputEditText: TextInputEditText
    private lateinit var textWatcher: TextWatcher
    private lateinit var viewModel: AddPostViewModel

    // TODO callback function not interface
    interface PostResultListener {
        fun onPostSaved(postText: String, fileName: String)
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

        viewModel = (activity as HomeActivity).getAddPostViewModel()
        binding.addPostViewModel = viewModel

        setupTextInput()

        val title = "New memo"
        val builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)
        builder.setTitle(title)
        builder.setMessage("What would you like to tell me?")
        builder.setPositiveButton("Save") { _, _ ->
            viewModel.currentText.get()?.let {
                resultListener.onPostSaved(it, viewModel.fileName)
            } ?: run {
                Log.e(TAG, "Post text is null.")
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog?.dismiss()
        }

        return builder.create()
    }

    override fun onStop() {
        viewModel.onStop()
        textInputEditText.removeTextChangedListener(textWatcher)
        super.onStop()
    }

    private fun setupTextInput() {
        textInputEditText = binding.root.findViewById(R.id.post_edit_text)
        textWatcher = getTextWatcher()
        textInputEditText.addTextChangedListener(textWatcher)
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.currentText.set(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // no-op
            }
        }
    }
}
