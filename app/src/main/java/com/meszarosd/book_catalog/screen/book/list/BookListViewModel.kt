package com.meszarosd.book_catalog.screen.book.list

import android.util.Log
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

    fun applySort(){
        val books = _uiState.value.filteredBooks
        val sortedBooks = if(_uiState.value.isAscendingSorting){
            books.sortedWith(compareBy(_uiState.value.selectedOption.selector))
        } else {
            books.sortedWith(compareByDescending(_uiState.value.selectedOption.selector))
        }

        _uiState.update {
            it.apply{
                filteredBooks = sortedBooks
            }
        }
    }

    fun setIsAscending(value: Boolean){
        _uiState.update {
            it.copy(
                isAscendingSorting = value
            )
        }
    }

    fun setSelectedOption(opt: SortOptions<BookWithAuthor, *>){
        _uiState.update {
            it.copy(
                selectedOption = opt
            )
        }
    }

    private fun applyGenreFilter(){
        _uiState.update {current ->
            current.copy(
                filteredBooks = if(current.selectedGenres.isEmpty()){
                    current.books
                }else{
                    current.books.filter {it.genre in current.selectedGenres}
                }
            )
        }
    }

    fun toggleGenreFilter(genre: Genre){
        _uiState.update {
            val newSet = it.selectedGenres.toMutableSet()
            if(genre in newSet){
                newSet -= genre
            }else{
                newSet += genre
            }
            it.copy(
                selectedGenres = newSet
            )
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

    suspend fun fetchGenres(){
        val fetchedGenres = withContext(Dispatchers.IO) {
            bookRepository.getAllGenre()
        }
        _uiState.update { current ->
            current.copy(
                genres = fetchedGenres
            )
        }
    }
}