package com.meszarosd.book_catalog.screen.book.details

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meszarosd.book_catalog.BookRepository
import com.meszarosd.book_catalog.entities.BookReadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookDetailsUiState())
    val uiState: StateFlow<BookDetailsUiState> = _uiState

    fun getBook(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val book = bookRepository.getBookWithAuthor(id)
            _uiState.value = _uiState.value.copy(
                book = book
            )
        }
    }

    fun updateBookReadState(brs: BookReadState, bookId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            bookRepository.updateBookReadState(brs, bookId)
            if(brs == BookReadState.FINISHED_READING){
                bookRepository.updateYearFinished(bookId, Calendar.getInstance().get(Calendar.YEAR))
            }else{
                bookRepository.updateYearFinished(bookId, null)
            }
            updateBook(bookId)
        }
    }

    fun updateRating(bookId: Int, rating: Int){
        viewModelScope.launch(Dispatchers.IO) {
            bookRepository.updateUserRating(bookId, rating)
            updateBook(bookId)
        }
    }

    fun updateYearRead(bookId: Int, year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            bookRepository.updateYearFinished(bookId, year)
            updateBook(bookId)
        }
    }

    fun deleteBook(){
        if(_uiState.value.book == null) return
        viewModelScope.launch(Dispatchers.IO) {
            bookRepository.deleteBook(_uiState.value.book!!.book)
        }
    }

    private suspend fun updateBook(bookId: Int){
        _uiState.update { current ->
            current.copy(
                book = bookRepository.getBookWithAuthor(bookId)
            )
        }
    }

}