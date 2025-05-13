package com.meszarosd.book_catalog.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(System.currentTimeMillis())
)
