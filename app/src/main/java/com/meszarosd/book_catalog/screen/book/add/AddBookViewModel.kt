package com.meszarosd.book_catalog.screen.book.add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meszarosd.book_catalog.BookRepository
import com.meszarosd.book_catalog.entities.Author
import com.meszarosd.book_catalog.entities.Book
import com.meszarosd.book_catalog.entities.BookReadState
import com.meszarosd.book_catalog.entities.Genre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthorSearchResult(
    var query: String = "",
    var list: List<Author> = emptyList()
)

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private val _uiState = mutableStateOf(AddBookUiState())
    val uiState: State<AddBookUiState> = _uiState

    fun changeInputTitle(new: String){
        _uiState.value = _uiState.value.copy(
            inputTitle = new
        )
    }

    fun changeInputAuthor(new: String){
        _uiState.value = _uiState.value.copy(
            inputAuthor = new
        )
    }

    fun changeSelectedGenre(genre: Genre){
        _uiState.value = _uiState.value.copy(
            selectedGenre = genre
        )
    }

    fun insertBook(selectedAuthor: Author?){
        viewModelScope.launch(Dispatchers.IO) {
            val authorId = selectedAuthor?.id
                ?: bookRepository.insertAuthor(Author(name = _uiState.value.inputAuthor)).toInt()
            val newBook = Book(
                title = _uiState.value.inputTitle,
                authorId = authorId,
                bookReadState = BookReadState.NOT_READ,
                genreId = _uiState.value.selectedGenre?.id ?: 1
            )
            bookRepository.insertBook(newBook)
            changeInputTitle("")
            changeInputAuthor("")
        }
    }

    fun searchAuthor(query: String){
        viewModelScope.launch(Dispatchers.IO) {
            val searchResult = bookRepository.searchAuthor(query)
            _uiState.value = _uiState.value.copy(
                authorSearchResult = AuthorSearchResult(query, searchResult)
            )
        }
    }

    fun clearAuthorSearchResult(){
        _uiState.value = _uiState.value.copy(
            authorSearchResult = AuthorSearchResult()
        )
    }

    fun fetchGenres(){
        viewModelScope.launch(Dispatchers.IO){
            val genres = bookRepository.getAllGenre()
            _uiState.value = _uiState.value.copy(
                genres = genres
            )
        }
    }
}