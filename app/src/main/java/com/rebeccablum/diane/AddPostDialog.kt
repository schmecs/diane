package com.rebeccablum.diane

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.rebeccablum.diane.databinding.DialogAddPostBinding
import com.rebeccablum.diane.viewmodels.AddPostViewModel

// TODO handle recording on rotation
class AddPostDialog : DialogFragment() {

    companion object {
        val TAG = AddPostDialog::class.java.simpleName
    }

    private val viewModel: AddPostViewModel by lazy {
        (activity as HomeActivity).getAddPostViewModel()
    }

    private lateinit var binding: DialogAddPostBinding
    private lateinit var textInputEditText: TextInputEditText
    private lateinit var textWatcher: TextWatcher

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.dialog_add_post,
            null,
            false
        )

        binding.addPostViewModel = viewModel

        setupTextInput()

        val title = "New memo"
        val builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)
        builder.setTitle(title)
        builder.setMessage("What would you like to tell me?")
        builder.setPositiveButton("Save") { _, _ ->
            viewModel.onSave()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            viewModel.onCancel()
            dialog?.dismiss()
        }

        return builder.create()
    }

    private fun setupTextInput() {
        textInputEditText = binding.root.findViewById(R.id.post_edit_text)
        if (!TextUtils.isEmpty(viewModel.currentText.get())) {
            textInputEditText.setText(viewModel.currentText.get())
        }
        textWatcher = getTextWatcher()
        textInputEditText.addTextChangedListener(textWatcher)
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.onDismiss()
        textInputEditText.removeTextChangedListener(textWatcher)
        super.onDismiss(dialog)
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onTextChanged(s.toString())
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
