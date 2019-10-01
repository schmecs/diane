package com.rebeccablum.diane

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel

class AddPostViewModel(context: Application) : AndroidViewModel(context) {

    val currentText = ObservableField("")

    fun onTextChanged(s: CharSequence) {
        currentText.set(s.toString())
    }
}