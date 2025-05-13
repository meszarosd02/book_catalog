package com.meszarosd.book_catalog.screen.author.details

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
class AuthorDetailsViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel() {
    private val _uiState = mutableStateOf(AuthorDetailsUiState())
    val uiState: State<AuthorDetailsUiState> = _uiState

    fun getAuthor(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val author = bookRepository.getAuthorById(id)
            val count = bookRepository.getAuthorBookCount(id)

            _uiState.value = _uiState.value.copy(
                author = author,
                bookCount = count
            )
        }
    }

    fun deleteAuthor(){
        viewModelScope.launch(Dispatchers.IO) {
            if(_uiState.value.author == null) return@launch
            bookRepository.deleteAuthor(_uiState.value.author!!)
        }
    }
}