package com.meszarosd.book_catalog.screen.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meszarosd.book_catalog.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel(){
    private val _uiState = mutableStateOf(HomeUiState())
    val uiState: State<HomeUiState> = _uiState

    fun fetchBooks(){
        Log.d("HomeViewModel", "fetchBooks() start")
        viewModelScope.launch(Dispatchers.IO) {
            val books = bookRepository.getRecentlyUpdatedBooks()
            Log.d("HomeViewModel", "fetchBooks() fetched")
            _uiState.value = _uiState.value.copy(
                recentlyUpdatedBooks = books
            )
            Log.d("HomeViewModel", "fetchBooks() uistate updated")
        }
    }

    fun fetchTotalBooks(){
        viewModelScope.launch(Dispatchers.IO){
            val totalBooks = bookRepository.getBookCount()
            _uiState.value = _uiState.value.copy(
                totalBooks = totalBooks
            )
        }
    }

    fun fetchBookReadStateCount(){
        viewModelScope.launch(Dispatchers.IO){
            val bookReadStateCount = bookRepository.getBookCountForEachBookReadState()
            _uiState.value = _uiState.value.copy(
                bookReadStateCount = bookReadStateCount
            )
        }
    }
}