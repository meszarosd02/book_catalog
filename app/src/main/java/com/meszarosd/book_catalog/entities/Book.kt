package com.meszarosd.book_catalog.entities

import androidx.annotation.StringRes
import java.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.meszarosd.book_catalog.R
import java.util.Date

@Entity(
    tableName = "books",
    foreignKeys = [
        ForeignKey(
        entity = Author::class,
        parentColumns = ["id"],
        childColumns = ["author_id"],
        onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
        entity = Genre::class,
        parentColumns = ["id"],
        childColumns = ["genre_id"],
        onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    @ColumnInfo(name = "author_id")
    val authorId: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(name = "book_read_state")
    val bookReadState: BookReadState = BookReadState.NOT_READ,
    @ColumnInfo(name = "user_rating")
    val userRating: Int = 0,
    @ColumnInfo(name = "genre_id")
    val genreId: Int = 0,
    @ColumnInfo(name = "year_read")
    val yearRead: Int? = null
)

enum class BookReadState(@StringRes val displayNameRes: Int) {
    NOT_READ(R.string.book_read_state_not_read),
    CURRENTLY_READING(R.string.book_read_state_currently_reading),
    FINISHED_READING(R.string.book_read_state_finished)
}