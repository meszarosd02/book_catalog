package com.meszarosd.book_catalog.entities

import androidx.room.Embedded
import androidx.room.Relation

data class BookWithAuthor(
    @Embedded val book: Book,
    @Relation(
        parentColumn = "author_id",
        entityColumn = "id"
    )
    val author: Author,
    @Relation(
        parentColumn = "genre_id",
        entityColumn = "id"
    )
    val genre: Genre
) {
    constructor(
        bookId: Int,
        bookTitle: String,
        bookReadState: BookReadState,
        authorId: Int,
        authorName: String,
        genreId: Int = 0,
        genreName: String = "Teszt Mufaj"
    ) : this(
        Book(
            bookId, bookTitle, authorId, bookReadState = bookReadState, genreId = genreId
        ),
        Author(
            authorId, authorName
        ),
        Genre(
            genreId, genreName)
    )
}

data class BookFilter(
    val isRead: Boolean? = null
)

typealias BookPredicate = (BookWithAuthor) -> Boolean

fun BookFilter.buildPredicate(): BookPredicate{
    return { book ->
        val isRead = isRead?.let { book.book.bookReadState == BookReadState.NOT_READ } ?: true
        isRead
    }
}
