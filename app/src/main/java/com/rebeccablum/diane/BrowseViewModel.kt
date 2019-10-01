package com.rebeccablum.diane

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class BrowseViewModel(context: Application, private val repository: PostRepository) :
    AndroidViewModel(context) {

    val posts: LiveData<List<Post>>

    init {
        posts = getPostsFromRepository()
    }

    fun getPostsFromRepository(): LiveData<List<Post>> {
        return repository.getPosts()
    }
}
