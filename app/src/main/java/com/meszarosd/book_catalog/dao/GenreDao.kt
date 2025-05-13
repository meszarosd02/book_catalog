package com.meszarosd.book_catalog.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.meszarosd.book_catalog.entities.Genre

@Dao
interface GenreDao {
    @Query("select * from genres")
    suspend fun getAllGenre(): List<Genre>

    @Query("select count(*) from genres")
    suspend fun getGenreCount(): Int

    @Insert
    suspend fun insertGenre(genre: Genre)

    @Delete
    suspend fun deleteGenre(genre: Genre)
}