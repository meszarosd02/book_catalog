package com.meszarosd.book_catalog.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.meszarosd.book_catalog.entities.Book
import com.meszarosd.book_catalog.entities.BookReadState
import com.meszarosd.book_catalog.entities.BookWithAuthor

@Dao
interface BookDao {
    @Query("select * from books")
    suspend fun getAllBooks(): List<Book>

    @Transaction
    @Query("select * from books")
    suspend fun getAllBookWithAuthor(): List<BookWithAuthor>

    @Query("select * from books where id=:id")
    suspend fun getBookById(id: Int): Book

    @Transaction
    @Query("select * from books where id=:id")
    suspend fun getBookWithAuthor(id: Int): BookWithAuthor

    @Query("update books set book_read_state=:brs, updated_at=:updatedAt where id=:bookId")
    suspend fun updateBookReadState(brs: BookReadState, bookId: Int, updatedAt: Long = System.currentTimeMillis())

    @Query("update books set year_read=:year, updated_at=:updatedAt where id=:bookId")
    suspend fun updateYearFinished(bookId: Int, year: Int?, updatedAt: Long = System.currentTimeMillis())

    @Query("update books set user_rating=:userRating, updated_at=:updatedAt where id=:bookId")
    suspend fun updateUserRating(bookId: Int, userRating: Int, updatedAt: Long = System.currentTimeMillis())

    @Transaction
    @Query("select * from books order by updated_at desc limit 3")
    suspend fun recentlyUpdatedBooks(): List<BookWithAuthor>

    @Query("select count(*) from books")
    suspend fun getBookCount(): Int

    @Query("select count(*) from books where book_read_state=:bookReadState")
    suspend fun getBookCountByBookReadState(bookReadState: BookReadState): Int

    @Insert
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)
}