package com.meszarosd.book_catalog.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
data class Author(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)