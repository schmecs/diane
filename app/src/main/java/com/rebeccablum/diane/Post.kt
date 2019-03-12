package com.rebeccablum.diane

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.util.*

@Entity
data class Post(
        @ColumnInfo(name = "content") val content: String,
        @ColumnInfo(name = "created_at") val timestamp: Long = System.currentTimeMillis(),
        @PrimaryKey @ColumnInfo(name = "post_id") val id: String = UUID.randomUUID().toString()) {

    val formattedDateString: String
        get() = DateFormat.getDateInstance().format(Date(timestamp))
}
