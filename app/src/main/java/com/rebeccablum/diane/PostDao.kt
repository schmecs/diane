package com.rebeccablum.diane

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface PostDao {

    @Query("SELECT * FROM post")
    fun all(): LiveData<List<Post>>

    @Insert
    fun insertAll(users: List<Post>)

    @Insert
    fun insert(post: Post)

    @Update
    fun update(post: Post)

    @Delete
    fun delete(post: Post)

    @Query("DELETE FROM post")
    fun deleteAll()
}
