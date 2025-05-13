package com.meszarosd.book_catalog.screen.book.list

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meszarosd.book_catalog.BookRepository
import com.meszarosd.book_catalog.entities.BookWithAuthor
import com.meszarosd.book_catalog.entities.Genre
import com.meszarosd.book_catalog.lib.SortOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(BookListUiState())
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    val isAscendingSorting = mutableStateOf(false)

    private val _selectedGenres = MutableStateFlow<Set<Genre>>(emptySet())
    val selectedGenres: StateFlow<Set<Genre>> = _selectedGenres.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        Log.d("BookListViewModel", "viewModel cleared")
        Log.d("BookListViewModel", selectedGenres.toString())
    }

    /**
     * For any new fields just add the corresponding identifier (shown in the "Sort by" spinner),
     * and the way to access the field
     * The rest if done by the [applySort] function.
     * [SortOptions] for identifying the fields, [applySort] for doing the sorting
     */

    val sortOptions = listOf(
        SortOptions<BookWithAuthor, Int>("ID"){ it.book.id },
        SortOptions<BookWithAuthor, String>("Title"){ it.book.title.toLowerCase(Locale.current) },
        SortOptions("Author name"){ it.author.name },
        SortOptions("Created at"){it.book.createdAt},
        SortOptions("Updated at"){it.book.updatedAt}
    )

    fun applySort(option: SortOptions<BookWithAuthor, *>, ascending: Boolean){
        val books = _uiState.value.filteredBooks
        val sortedBooks = if(ascending){
            books.sortedWith(compareBy(option.selector))
        } else {
            books.sortedWith(compareByDescending(option.selector))
        }

        _uiState.update {
            _uiState.value.copy(
                filteredBooks = sortedBooks
            )
        }
    }

    private fun applyGenreFilter(){
        val currentGenres = _selectedGenres.value
        Log.d("currentGenres", currentGenres.toString())
        _uiState.update {current ->
            current.copy(
                filteredBooks = if(currentGenres.isEmpty()){
                    current.books
                }else{
                    current.books.filter {it.genre in currentGenres}
                }
            )
        }
        Log.d("books", _uiState.value.filteredBooks.toString())
    }

    fun toggleGenreFilter(genre: Genre){
        _selectedGenres.update { current ->
            if(genre in current) current - genre else current + genre
        }
        applyGenreFilter()
    }

    suspend fun fetchBooks(){
        val fetchedBooks = withContext(Dispatchers.IO){
            bookRepository.getAllBookWithAuthor()
        }
        _uiState.update { current ->
            current.copy(
                books = fetchedBooks,
                filteredBooks = fetchedBooks
            )
        }
    }

    fun fetchGenres(){
        viewModelScope.launch(Dispatchers.IO){
            val fetchedGenres = bookRepository.getAllGenre()
            _uiState.value = _uiState.value.copy(
                genres = fetchedGenres
            )
        }
    }

    fun addBook() {
        TODO("Not yet implemented")
    }
}