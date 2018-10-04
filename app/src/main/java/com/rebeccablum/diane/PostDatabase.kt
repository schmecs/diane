package com.rebeccablum.diane

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = arrayOf(Post::class), version = 1)
abstract class PostDatabase : RoomDatabase() {

    abstract fun PostDao(): PostDao

    companion object {
        private val DB_NAME = "PostDatabase"

        @Volatile
        private var INSTANCE: PostDatabase? = null

        fun getInstance(context: Context): PostDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also {
                        INSTANCE = it
                    }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        PostDatabase::class.java, DB_NAME)
                        .build()
    }
}
