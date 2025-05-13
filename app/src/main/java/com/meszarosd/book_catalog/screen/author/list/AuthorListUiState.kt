package com.meszarosd.book_catalog.screen.author.list

import com.meszarosd.book_catalog.entities.Author

data class AuthorListUiState(
    val authors: List<Author> = emptyList()
)
