package com.meszarosd.book_catalog.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AuthorWithBooks(
    @Embedded val author: Author,
    @Relation(
        parentColumn = "id",
        entityColumn = "authorId"
    )
    val books: List<Book>
)
