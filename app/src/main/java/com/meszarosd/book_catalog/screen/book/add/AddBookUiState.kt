package com.meszarosd.book_catalog.screen.book.add

import com.meszarosd.book_catalog.entities.Genre

data class AddBookUiState(
    val inputTitle: String = "",
    val inputAuthor: String = "",
    val authorSearchResult: AuthorSearchResult = AuthorSearchResult(),
    val genres: List<Genre> = listOf(),
    val selectedGenre: Genre? = null
)
