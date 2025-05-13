package com.meszarosd.book_catalog.screen.book.list

import com.meszarosd.book_catalog.entities.BookFilter
import com.meszarosd.book_catalog.entities.BookWithAuthor
import com.meszarosd.book_catalog.entities.Genre

data class BookListUiState(
    val books: List<BookWithAuthor> = listOf(),
    var filteredBooks: List<BookWithAuthor> = mutableListOf(),
    var bookFilter: BookFilter = BookFilter(),
    var isBookAddSheetExtended: Boolean = false,
    val genres: List<Genre> = listOf()
)
