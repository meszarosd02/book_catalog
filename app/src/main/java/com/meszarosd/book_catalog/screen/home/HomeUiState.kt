package com.meszarosd.book_catalog.screen.home

import com.meszarosd.book_catalog.entities.BookReadState
import com.meszarosd.book_catalog.entities.BookWithAuthor

data class HomeUiState(
    val recentlyUpdatedBooks: List<BookWithAuthor> = listOf(),
    val totalBooks: Int = 0,
    val bookReadStateCount: Map<BookReadState, Int> = emptyMap()
)
