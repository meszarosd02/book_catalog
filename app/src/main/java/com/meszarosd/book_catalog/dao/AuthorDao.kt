package com.meszarosd.book_catalog.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.meszarosd.book_catalog.entities.Author

@Dao
interface AuthorDao {
    @Query("select * from authors")
    suspend fun getAllAuthors(): List<Author>

    @Query("select * from authors where id=:id")
    suspend fun getAuthorById(id: Int): Author

    @Query("select count(*) from books where author_id=:id")
    suspend fun getAuthorBookCount(id: Int): Int

    @Query("select * from authors where name like '%' || :query || '%'")
    suspend fun searchAuthor(query: String): List<Author>

    @Insert
    suspend fun insertAuthor(author: Author): Long

    @Delete
    suspend fun deleteAuthor(author: Author)
}