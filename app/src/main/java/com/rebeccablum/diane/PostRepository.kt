package com.rebeccablum.diane

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import javax.inject.Singleton

@Singleton
class PostRepository(application: Application) {

    private var postDao: PostDao
    private var postData: LiveData<List<Post>>

    init {
        val db = PostDatabase.getInstance(application)
        postDao = db.PostDao()
        postData = postDao.all()
    }

    fun getPosts(): LiveData<List<Post>> {
        return postData
    }

    fun insertPost(post: Post) {
        InsertAsyncTask(postDao).execute(post)
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

    internal class InsertAsyncTask(private val asyncTaskDao: PostDao) :
        AsyncTask<Post, Void, Void>() {

        override fun doInBackground(vararg params: Post): Void? {
            asyncTaskDao.insert(params[0])
            return null
        }
    }
}
