package com.meszarosd.book_catalog

import android.util.Log
import com.meszarosd.book_catalog.dao.AuthorDao
import com.meszarosd.book_catalog.dao.BookDao
import com.meszarosd.book_catalog.dao.GenreDao
import com.meszarosd.book_catalog.entities.Author
import com.meszarosd.book_catalog.entities.Book
import com.meszarosd.book_catalog.entities.BookReadState
import com.meszarosd.book_catalog.entities.BookWithAuthor
import com.meszarosd.book_catalog.entities.Genre
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao,
    private val authorDao: AuthorDao,
    private val genreDao: GenreDao
) {
    suspend fun getAllBooks(): List<Book>{
        return bookDao.getAllBooks()
    }

    suspend fun getAllBookWithAuthor(): List<BookWithAuthor>{
        Log.d("books", "get all books")
        return bookDao.getAllBookWithAuthor()
    }

    suspend fun getBookById(id: Int): Book{
        return bookDao.getBookById(id)
    }

    suspend fun getBookCount(): Int{
        return bookDao.getBookCount()
    }

    suspend fun getBookCountForEachBookReadState(): Map<BookReadState, Int>{
        return BookReadState.entries.associateWith { bookReadState -> bookDao.getBookCountByBookReadState(bookReadState) }
    }

    suspend fun updateBookReadState(bookReadState: BookReadState, bookId: Int){
        bookDao.updateBookReadState(bookReadState, bookId)
    }

    suspend fun getBookWithAuthor(id: Int): BookWithAuthor{
        return bookDao.getBookWithAuthor(id)
    }

    suspend fun getRecentlyUpdatedBooks(): List<BookWithAuthor>{
        Log.d("books", "getRecentlyUpdatedBooks")
        return bookDao.recentlyUpdatedBooks()
    }

    suspend fun updateUserRating(id: Int, rating: Int){
        return bookDao.updateUserRating(id, rating)
    }

    suspend fun updateYearFinished(id: Int, year: Int?){
        return bookDao.updateYearFinished(id, year)
    }

    suspend fun insertBook(book: Book)
    {
        bookDao.insertBook(book)
    }

    suspend fun deleteBook(book: Book){
        bookDao.deleteBook(book)
    }

    suspend fun insertAuthor(author: Author): Long{
        return authorDao.insertAuthor(author)
    }

    suspend fun deleteAuthor(author: Author){
        return authorDao.deleteAuthor(author)
    }

    suspend fun searchAuthor(query: String): List<Author> {
        return authorDao.searchAuthor(query)
    }

    suspend fun getAllAuthors(): List<Author>{
        return authorDao.getAllAuthors()
    }

    suspend fun getAuthorById(id: Int): Author{
        return authorDao.getAuthorById(id)
    }

    suspend fun getAuthorBookCount(id: Int): Int{
        return authorDao.getAuthorBookCount(id)
    }

    suspend fun getAllGenre(): List<Genre> {
        return genreDao.getAllGenre()
    }
}