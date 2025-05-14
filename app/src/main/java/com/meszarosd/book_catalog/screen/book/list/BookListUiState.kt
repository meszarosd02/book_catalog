package com.meszarosd.book_catalog.screen.book.list

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import com.meszarosd.book_catalog.entities.BookFilter
import com.meszarosd.book_catalog.entities.BookWithAuthor
import com.meszarosd.book_catalog.entities.Genre
import com.meszarosd.book_catalog.lib.SortOptions

/**
 * For any new fields just add the corresponding identifier (shown in the "Sort by" spinner),
 * and the way to access the field.
 * The rest if done by the [applySort] function.
 * [SortOptions] for identifying the fields, [applySort] for doing the sorting
 */
private val DEFAULT_SORT_OPTIONS: List<SortOptions<BookWithAuthor, *>> = listOf(
    SortOptions("ID"){ it.book.id },
    SortOptions("Title"){ it.book.title.toLowerCase(Locale.current) },
    SortOptions("Author name"){ it.author.name },
    SortOptions("Created at"){it.book.createdAt},
    SortOptions("Updated at"){it.book.updatedAt}
)

private val DEFAULT_SELECTED_OPTION = DEFAULT_SORT_OPTIONS.first()

data class BookListUiState(
    val books: List<BookWithAuthor> = listOf(),
    var filteredBooks: List<BookWithAuthor> = mutableListOf(),
    var bookFilter: BookFilter = BookFilter(),
    var isBookAddSheetExtended: Boolean = false,
    val genres: List<Genre> = listOf(),
    val selectedGenres: Set<Genre> = emptySet(),
    val isAscendingSorting: Boolean = true,
    val sortOptions: List<SortOptions<BookWithAuthor, *>> = DEFAULT_SORT_OPTIONS,
    val selectedOption: SortOptions<BookWithAuthor, *> = DEFAULT_SELECTED_OPTION
)
