package com.rebeccablum.diane

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repository: PostRepository) : ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val mutablePostData = MutableLiveData<List<Post>>()
    val postData: LiveData<List<Post>> by lazy {
        fetchPosts()
        return@lazy mutablePostData
    }

    val isAddingPost = ObservableBoolean(false)
    internal val addPostClickEvent = SingleLiveEvent<Void>()

    fun onClick() {
        addPostClickEvent.call()
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            repository.insertPost(post)
            withContext(Dispatchers.Main) {
                onPostAdded()
            }
        }
    }

    fun onPostAdded() {
        isAddingPost.set(false)
        fetchPosts()
    }

    fun onPostCancelled() {
        isAddingPost.set(false)
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            mutablePostData.value = repository.fetchPosts()
        }
    }
}
