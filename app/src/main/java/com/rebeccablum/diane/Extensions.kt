package com.rebeccablum.diane

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

inline fun <T> ObservableField<T>.onChanged(crossinline callback: (T?) -> Unit) {
    addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            callback.invoke(get())
        }
    })
}

inline fun ObservableBoolean.onChanged(crossinline callback: (Boolean) -> Unit) {
    addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            callback.invoke(get())
        }
    })
}
