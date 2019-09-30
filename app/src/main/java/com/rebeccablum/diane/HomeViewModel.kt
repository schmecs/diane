package com.rebeccablum.diane

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel(context: Application, private val repository: PostRepository) :
    AndroidViewModel(context) {

    val isAddingPost = ObservableBoolean(false)

    fun onClick() {
        isAddingPost.set(true)
    }

    fun addPost(post: Post) {
        GlobalScope.launch {
            repository.insertPost(post)
        }
        setDoneAddingPost()
    }

    fun setDoneAddingPost() {
        isAddingPost.set(false)
    }
}
