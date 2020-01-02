package com.rebeccablum.diane.data

import android.app.Application
import com.rebeccablum.diane.models.Post
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

        val MOCK_POST_DATA = listOf(
            Post(content = "post1", fileName = "test_file_name_1"),
            Post(content = "post2", fileName = "test_file_name_2")
        )

        @JvmStatic
        fun getInstance(application: Application) =
            INSTANCE
                ?: synchronized(PostRepository::class.java) {
                INSTANCE
                    ?: PostRepository(application)
                    .also { INSTANCE = it }
            }
    }
}
