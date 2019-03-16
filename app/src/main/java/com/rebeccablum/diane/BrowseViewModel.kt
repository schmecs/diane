package com.rebeccablum.diane

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class BrowseViewModel(context: Application, private val repository: PostRepository) : AndroidViewModel(context) {

    private val posts: LiveData<List<Post>>

    init {
        posts = getPosts()
    }

    fun getPosts(): LiveData<List<Post>> {
        return repository.getPosts()
    }
}
