package com.meszarosd.book_catalog

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.meszarosd.book_catalog.dao.AuthorDao
import com.meszarosd.book_catalog.dao.BookDao
import com.meszarosd.book_catalog.dao.GenreDao
import com.meszarosd.book_catalog.entities.Author
import com.meszarosd.book_catalog.entities.Book
import com.meszarosd.book_catalog.entities.BookReadState
import com.meszarosd.book_catalog.entities.Genre
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Year
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

class Converters{
    @TypeConverter
    fun fromTimestamp(value: Long): Date{
        return Date(value)
    }
    @TypeConverter
    fun dateToTimestamp(date: Date): Long{
        return date.time
    }
    @TypeConverter
    fun toBookReadState(value: Int): BookReadState{
        return BookReadState.entries[value]
    }
    @TypeConverter
    fun fromBookReadState(brs: BookReadState): Int{
        return BookReadState.entries.indexOf(brs)
    }
}

@Database(entities = [Book::class, Author::class, Genre::class], version = 8, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BookDatabase : RoomDatabase(){
    abstract fun bookDao(): BookDao
    abstract fun authorDao(): AuthorDao
    abstract fun genreDao(): GenreDao
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideBookDatabase(
        @ApplicationContext context: Context
        ): BookDatabase {
        lateinit var dbInstance: BookDatabase
        val callback = object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val genreDao = dbInstance.genreDao()
                    Log.d("genres", genreDao.getGenreCount().toString())
                    if(genreDao.getGenreCount() == 0) {
                        genreDao.insertGenre(Genre(name = context.getString(R.string.default_genre_fantasy)))
                        genreDao.insertGenre(Genre(name = context.getString(R.string.default_genre_scifi)))
                        genreDao.insertGenre(Genre(name = context.getString(R.string.default_genre_romance)))
                        genreDao.insertGenre(Genre(name = context.getString(R.string.default_genre_thriller)))
                        genreDao.insertGenre(Genre(name = context.getString(R.string.default_genre_horror)))
                        genreDao.insertGenre(Genre(name = context.getString(R.string.default_genre_mystery)))
                        genreDao.insertGenre(Genre(name = context.getString(R.string.default_genre_literary_fiction)))
                        genreDao.insertGenre(Genre(name = context.getString(R.string.default_genre_action_adventure)))
                        genreDao.insertGenre(Genre(name = context.getString(R.string.default_genre_historical_fiction)))
                        genreDao.insertGenre(Genre(name = context.getString(R.string.default_genre_crime_fiction)))
                        genreDao.insertGenre(Genre(name = context.getString(R.string.default_genre_other)))
                    }
                }
            }
        }
        dbInstance = Room.databaseBuilder(context, BookDatabase::class.java, "book_db")
            .addCallback(callback)
            .addMigrations(MIGR_1_2, MIGR_2_3, MIGR_3_4, MIGR_4_5, MIGR_5_6, MIGR_6_7, MIGR_7_8)
            .build()
        return dbInstance
    }

    @Singleton
    @Provides
    fun provideBookDao(db: BookDatabase) = db.bookDao()

    @Singleton
    @Provides
    fun provideAuthorDao(db: BookDatabase) = db.authorDao()

    @Singleton
    @Provides
    fun provideGenreDao(db: BookDatabase) = db.genreDao()
}

@HiltAndroidApp
class MyApplication : Application() {

}

private val MIGR_1_2 = object : Migration(1,2){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""create table temp_books(
            |id integer primary key autoincrement not null, 
            |title text not null, 
            |author_id integer not null, 
            |is_read integer not null default 0,
            |foreign key(author_id) references authors(id) on delete cascade)""".trimMargin())
        db.execSQL("insert into temp_books (id, title, author_id) select id, title, authorId from books")
        db.execSQL("drop table books")
        db.execSQL("alter table temp_books rename to books")
    }
}

private val MIGR_2_3 = object : Migration(2, 3){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            create table temp_books(
            id integer primary key autoincrement not null, 
            title text not null, 
            author_id integer not null, 
            is_read integer not null default 0,
            created_at integer not null default ${System.currentTimeMillis()},
            updated_at integer not null default ${System.currentTimeMillis()},
            foreign key(author_id) references authors(id) on delete cascade
            )
        """.trimIndent())
        db.execSQL("insert into temp_books (id, title, author_id, is_read) select id, title, author_id, is_read from books")
        db.execSQL("drop table books")
        db.execSQL("alter table temp_books rename to books")
    }
}

private val MIGR_3_4 = object : Migration(3, 4){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            create table temp_books(
            id integer primary key autoincrement not null, 
            title text not null, 
            author_id integer not null,
            created_at integer not null default ${System.currentTimeMillis()},
            updated_at integer not null default ${System.currentTimeMillis()},
            book_read_state integer not null default 0,
            foreign key(author_id) references authors(id) on delete cascade
            )
        """.trimIndent())
        db.execSQL("""insert into temp_books (
            id, title, author_id, created_at, updated_at
            ) 
            select id, title, author_id, created_at, updated_at from books""".trimIndent())
        db.execSQL("""
            update temp_books set book_read_state=1 where id = (select id from books where is_read=1)
        """.trimIndent())
        db.execSQL("drop table books")
        db.execSQL("alter table temp_books rename to books")
    }
}

private val MIGR_4_5 = object : Migration(4, 5){
    override fun migrate(db: SupportSQLiteDatabase) {
        Log.d("db", "1")
        db.execSQL("""
            create table genres(
            id integer primary key autoincrement not null,
            name text not null,
            created_at integer not null default ${System.currentTimeMillis()},
            updated_at integer not null default ${System.currentTimeMillis()}
            )
        """.trimIndent())
        Log.d("db", "2")
        db.execSQL("""
            insert into genres (name) values ("Fantasy"), ("Sci-Fi"), ("Romance"), ("Thriller"), ("Horror"), 
            ("Mystery"), ("Literary Fiction"), ("Action & Adventure"), ("Historical Fiction"), ("Crime Fiction"), 
            ("Other")
        """.trimIndent())
        Log.d("db", "3")
        db.execSQL("""
            create table temp_books(
            id integer primary key autoincrement not null, 
            title text not null, 
            author_id integer not null,
            created_at integer not null default ${System.currentTimeMillis()},
            updated_at integer not null default ${System.currentTimeMillis()},
            book_read_state integer not null default 0,
            user_rating integer not null default 0,
            genre_id integer not null default 0,
            foreign key(author_id) references authors(id) on delete cascade,
            foreign key(genre_id) references genres(id) on delete cascade
            )
        """.trimIndent())
        Log.d("db", "4")
        db.execSQL("""insert into temp_books (
            id, title, author_id, created_at, updated_at, book_read_state
            ) 
            select id, title, author_id, created_at, updated_at, book_read_state from books""".trimIndent())
        Log.d("db", "5")
        db.execSQL("drop table books")
        db.execSQL("alter table temp_books rename to books")
    }
}

private val MIGR_5_6 = object : Migration(5, 6){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("update books set genre_id=1")
    }
}

private val MIGR_6_7 = object : Migration(6, 7){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            create table temp_books(
            id integer primary key autoincrement not null, 
            title text not null, 
            author_id integer not null,
            created_at integer not null default ${System.currentTimeMillis()},
            updated_at integer not null default ${System.currentTimeMillis()},
            book_read_state integer not null default 0,
            user_rating integer not null default 0,
            genre_id integer not null default 0,
            year_read integer not null default ${Calendar.getInstance().get(Calendar.YEAR)}
            foreign key(author_id) references authors(id) on delete cascade,
            foreign key(genre_id) references genres(id) on delete cascade
            )
        """.trimIndent())
        db.execSQL("""
            insert into temp_books (id, title, author_id, created_at, updated_at, book_read_state, user_rating, genre_id)
            select (id, title, author_id, created_at, updated_at, book_read_state, user_rating, genre_id) from books
        """.trimIndent())
        db.execSQL("drop table books")
        db.execSQL("alter table temp_books rename to books")
    }
}

private val MIGR_7_8 = object : Migration(7, 8){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            create table temp_books(
            id integer primary key autoincrement not null, 
            title text not null, 
            author_id integer not null,
            created_at integer not null default ${System.currentTimeMillis()},
            updated_at integer not null default ${System.currentTimeMillis()},
            book_read_state integer not null default 0,
            user_rating integer not null default 0,
            genre_id integer not null default 0,
            year_read integer,
            foreign key(author_id) references authors(id) on delete cascade,
            foreign key(genre_id) references genres(id) on delete cascade
            )
        """.trimIndent())
        db.execSQL("""
            insert into temp_books (id, title, author_id, created_at, updated_at, book_read_state, user_rating, genre_id, year_read)
            select id, title, author_id, created_at, updated_at, book_read_state, user_rating, genre_id, year_read from books
        """.trimIndent())
        db.execSQL("drop table books")
        db.execSQL("alter table temp_books rename to books")
    }
}