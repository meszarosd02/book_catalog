package com.meszarosd.book_catalog.screen.author.list

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
class AuthorListViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private val _uiState = mutableStateOf(AuthorListUiState())
    val uiState: State<AuthorListUiState> = _uiState

    fun fetchAuthors(onFinish: () -> Unit){
        viewModelScope.launch(Dispatchers.IO){
            val authors = bookRepository.getAllAuthors()
            _uiState.value = _uiState.value.copy(
                authors = authors
            )
            onFinish()
        }
    }
}