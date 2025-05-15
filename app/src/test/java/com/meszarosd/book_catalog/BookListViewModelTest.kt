package com.meszarosd.book_catalog

import androidx.lifecycle.viewModelScope
import com.meszarosd.book_catalog.entities.Author
import com.meszarosd.book_catalog.entities.Book
import com.meszarosd.book_catalog.entities.BookWithAuthor
import com.meszarosd.book_catalog.entities.Genre
import com.meszarosd.book_catalog.lib.SortOptions
import com.meszarosd.book_catalog.screen.book.list.BookListViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BookListViewModelTest {

    @Mock
    lateinit var bookRepository: BookRepository

    private lateinit var viewModel: BookListViewModel

    @Before
    fun setup(){
        viewModel = BookListViewModel(bookRepository)
    }

    @Test
    fun `fetch books to ui state`() = runTest{
        val testBook = listOf(
            BookWithAuthor(
                book = Book(1, "test book", 1),
                author = Author(1, "test author"),
                genre = Genre(1, "Fantasy")
            )
        )
        Mockito.`when`(bookRepository.getAllBookWithAuthor()).thenReturn(
            testBook
        )
        viewModel.fetchBooks()
        Assert.assertEquals(testBook, viewModel.uiState.value.books)
        Assert.assertEquals(testBook, viewModel.uiState.value.filteredBooks)
    }
    @Test
    fun `genre filter toggle`() {
        val testGenre1 = Genre(1, "Fantasy")
        val testGenre2 = Genre(2, "Mystery")
        val testGenre3 = Genre(3, "Crime Fiction")

        viewModel.toggleGenreFilter(testGenre1)
        Assert.assertEquals(1, viewModel.uiState.value.selectedGenres.size)

        viewModel.toggleGenreFilter(testGenre2)
        Assert.assertEquals(2, viewModel.uiState.value.selectedGenres.size)

        viewModel.toggleGenreFilter(testGenre1)
        Assert.assertEquals(1, viewModel.uiState.value.selectedGenres.size)

        viewModel.toggleGenreFilter(testGenre3)
        Assert.assertEquals(2, viewModel.uiState.value.selectedGenres.size)

        viewModel.toggleGenreFilter(testGenre1)
        Assert.assertEquals(3, viewModel.uiState.value.selectedGenres.size)

        viewModel.toggleGenreFilter(testGenre1)
        Assert.assertEquals(2, viewModel.uiState.value.selectedGenres.size)

        viewModel.toggleGenreFilter(testGenre3)
        Assert.assertEquals(1, viewModel.uiState.value.selectedGenres.size)

        viewModel.toggleGenreFilter(testGenre2)
        Assert.assertEquals(0, viewModel.uiState.value.selectedGenres.size)

    }
    @Test
    fun `filter books by genre`() = runTest{
        val testBooks = listOf(
            BookWithAuthor(
                book = Book(1, "test book", 1, genreId = 1),
                author = Author(1, "test author"),
                genre = Genre(1, "Fantasy")
            ),
            BookWithAuthor(
                book = Book(2, "test book_2", 1, genreId = 2),
                author = Author(1, "test author"),
                genre = Genre(2, "Mystery")
            )
        )

        val correctResult = listOf(
            BookWithAuthor(
                book = Book(2, "test book_2", 1, genreId = 2),
                author = Author(1, "test author"),
                genre = Genre(2, "Mystery")
            )
        )
        Mockito.`when`(bookRepository.getAllBookWithAuthor()).thenReturn(
            testBooks
        )
        viewModel.fetchBooks()
        viewModel.toggleGenreFilter(correctResult[0].genre)
        val result = viewModel.uiState.value.filteredBooks
        Assert.assertEquals(correctResult, result)
    }

    @Test
    fun `fetch genres`() = runTest{
        val testGenres = listOf(
            Genre(1, "Fantasy"),
            Genre(2, "Mystery")
        )
        Mockito.`when`(bookRepository.getAllGenre()).thenReturn(testGenres)
        viewModel.fetchGenres()
        Assert.assertEquals(testGenres, viewModel.uiState.value.genres)
    }

    @Test
    fun `sorting`() = runTest{
        val testBooks = listOf(
            BookWithAuthor(
                book = Book(1, "a test book", 1, genreId = 1),
                author = Author(1, "btest author"),
                genre = Genre(1, "Fantasy")
            ),
            BookWithAuthor(
                book = Book(2, "test book_2", 1, genreId = 2),
                author = Author(1, "test author"),
                genre = Genre(2, "Mystery")
            ),
            BookWithAuthor(
                book = Book(3, "aaa test book_3", 1, genreId = 2),
                author = Author(2, "atest author"),
                genre = Genre(2, "Mystery")
            )
        )

        val idSortOption = SortOptions<BookWithAuthor, Int>("ID"){it.book.id}
        val authorSortOption = SortOptions<BookWithAuthor, String>("Author name"){it.author.name}

        Mockito.`when`(bookRepository.getAllBookWithAuthor()).thenReturn(testBooks)

        viewModel.fetchBooks()

        //Sort by ID
        viewModel.setSelectedOption(idSortOption)
        Assert.assertEquals(idSortOption, viewModel.uiState.value.selectedOption)
        viewModel.setIsAscending(false)
        Assert.assertEquals(false, viewModel.uiState.value.isAscendingSorting)
        viewModel.applySort()
        Assert.assertEquals(testBooks.reversed(), viewModel.uiState.value.filteredBooks)

        //Sort by Author Name
        viewModel.setSelectedOption(authorSortOption)
        viewModel.setIsAscending(true)
        viewModel.applySort()
        Assert.assertEquals(testBooks.sortedBy { it.author.name }, viewModel.uiState.value.filteredBooks)
    }
}