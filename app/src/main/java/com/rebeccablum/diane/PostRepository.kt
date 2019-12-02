package com.rebeccablum.diane

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.coroutineScope
import javax.inject.Singleton

@Singleton
class PostRepository(application: Application) {

    private var postDao: PostDao

    init {
        val db = PostDatabase.getInstance(application)
        postDao = db.PostDao()
    }

    suspend fun fetchPosts(): List<Post> {
        return postDao.all()
    }

    suspend fun insertPost(post: Post) {
        postDao.insert(post)
    }

    companion object {
        private var INSTANCE: PostRepository? = null

        val MOCK_POST_DATA = listOf(Post("post1"), Post("post2"))

        @JvmStatic
        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(PostRepository::class.java) {
                INSTANCE ?: PostRepository(application)
                    .also { INSTANCE = it }
            }
    }
}
