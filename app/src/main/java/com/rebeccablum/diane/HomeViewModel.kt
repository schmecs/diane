package com.rebeccablum.diane

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel

class HomeViewModel(context: Application, private val repository: PostRepository) : AndroidViewModel(context) {

    val testString = ObservableField<String>("")

    init {
        testString.set("Hi")
    }

    fun onClick() {
        testString.set(if (testString.get() == "Hi") "Hello" else "Hi")
        addPost(Post("Test post"))
    }

    private fun addPost(post: Post) {
        repository.insertPost(post)
    }
}
