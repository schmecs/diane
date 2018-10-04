package com.rebeccablum.diane

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.databinding.ObservableField

class BrowseViewModel(application: Application) : AndroidViewModel(application) {

    private var posts: LiveData<List<Post>>
    private var postIdTemp: Int = 3

    val testString = ObservableField<String>("")

    private val repository = PostRepository(application)

    init {
        testString.set("Hi")
        posts = repository.getPosts()
    }

    fun onClick() {
        testString.set(if (testString.get() == "Hi") "Hello" else "Hi")
        addPost()
    }

    fun addPost() {
        repository.insertPost(Post(postIdTemp, "Test post"))
        postIdTemp++
    }

    fun getPosts(): LiveData<List<Post>> {
        return posts
    }
}
