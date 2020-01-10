package com.rebeccablum.diane.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class SignInViewModel : ViewModel() {

    val isLoggedIn = ObservableBoolean(false)
    val isInErrorState = ObservableBoolean(false)
    val name = ObservableField("Agent Cooper")
}