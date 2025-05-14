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

    fun applySort(option: SortOptions<BookWithAuthor, *>){
        val books = _uiState.value.filteredBooks
        val sortedBooks = if(_uiState.value.isAscendingSorting){
            books.sortedWith(compareBy(option.selector))
        } else {
            books.sortedWith(compareByDescending(option.selector))
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
        val currentGenres = _uiState.value.selectedGenres
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
        _uiState.update {
            val newSet = it.selectedGenres.toMutableSet()
            if(genre in it.selectedGenres){
                newSet += genre
            }else{
                newSet -= genre
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