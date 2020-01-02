package com.rebeccablum.diane.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebeccablum.diane.models.Post
import com.rebeccablum.diane.data.PostRepository
import com.rebeccablum.diane.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PostRepository) : ViewModel() {

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }

    private val mutablePostData = MutableLiveData<List<Post>>()
    val postData: LiveData<List<Post>> by lazy {
        fetchPosts()
        return@lazy mutablePostData
    }

    internal val addPostClickEvent = SingleLiveEvent<Void>()

    fun onClick() {
        addPostClickEvent.call()
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            repository.insertPost(post)
            // TODO what is the right way to reload posts after successful add?
            mutablePostData.value = repository.fetchPosts()
        }
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            mutablePostData.value = repository.fetchPosts()
        }
    }
}
