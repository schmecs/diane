package com.rebeccablum.diane.data

import androidx.room.*
import com.rebeccablum.diane.models.Post

@Dao
interface PostDao {

    @Query("SELECT * FROM post")
    suspend fun all(): List<Post>

    @Insert
    suspend fun insertAll(users: List<Post>)

    @Insert
    suspend fun insert(post: Post)

    @Update
    suspend fun update(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Query("DELETE FROM post")
    suspend fun deleteAll()
}
