package com.rebeccablum.diane

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask


class PostRepository(application: Application) {

    // TODO: hook this up to get and set data with database.

    private var postDao: PostDao
    private var postData: LiveData<List<Post>>

    init {
        val db = PostDatabase.getInstance(application)
        postDao = db.PostDao()
        postData = postDao.all()

        // Temporary
        insertPost(MOCK_POST_DATA[0])
        insertPost(MOCK_POST_DATA[1])
    }

    fun getPosts(): LiveData<List<Post>> {
        return postData
    }

    fun insertPost(post: Post) {
        InsertAsyncTask(postDao).execute(post)
    }

    companion object {
        val MOCK_POST_DATA = listOf(Post("post1"), Post("post2"))
    }

    internal class InsertAsyncTask(private val asyncTaskDao: PostDao) : AsyncTask<Post, Void, Void>() {

        override fun doInBackground(vararg params: Post): Void? {
            asyncTaskDao.insert(params[0])
            return null
        }
    }
}
