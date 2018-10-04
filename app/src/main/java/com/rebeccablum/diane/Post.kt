package com.rebeccablum.diane

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Post(
        @PrimaryKey(autoGenerate = true) val id: Int? = null,
        @ColumnInfo(name = "content") val content: String)