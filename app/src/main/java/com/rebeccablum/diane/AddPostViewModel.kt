package com.rebeccablum.diane

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class AddPostViewModel : ViewModel() {

    val currentText = ObservableField("")

    fun onTextChanged(s: CharSequence) {
        currentText.set(s.toString())
    }
}