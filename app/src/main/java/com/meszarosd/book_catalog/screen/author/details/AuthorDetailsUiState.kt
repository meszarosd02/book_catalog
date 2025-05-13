package com.meszarosd.book_catalog.screen.author.details

import com.meszarosd.book_catalog.entities.Author

data class AuthorDetailsUiState(
    val author: Author? = null,
    val bookCount: Int = 0
)
