@file:OptIn(ExperimentalMaterial3Api::class)

package com.meszarosd.book_catalog.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meszarosd.book_catalog.R
import com.meszarosd.book_catalog.entities.BookReadState
import com.meszarosd.book_catalog.entities.BookWithAuthor
import com.meszarosd.book_catalog.navigation.NavigationUIState
import com.meszarosd.book_catalog.navigation.topbar.icons.DrawerOpenIcon
import com.meszarosd.book_catalog.screen.BaseScreen
import com.meszarosd.book_catalog.screen.book.list.BookListCard

@Composable
fun HomeScreen(
    navigationUIState: NavigationUIState,
    onBookClicked: (bookId: Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
){
    val state by viewModel.uiState

    LaunchedEffect(null) {
        if(state.recentlyUpdatedBooks.isEmpty())
            viewModel.fetchBooks()
        viewModel.fetchTotalBooks()
        viewModel.fetchBookReadStateCount()
    }

    BaseScreen(
        title = stringResource(R.string.navigation_title_home),
        navigationUIState = navigationUIState,
        navigationIcon = {DrawerOpenIcon(navigationUIState.scope, navigationUIState.drawerState)}
    ) { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                item {
                    BookReadStateStatsCard(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                        state.totalBooks,
                        state.bookReadStateCount
                    )
                }
                item {
                    RecentlyUpdatedBooksCard(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        books = state.recentlyUpdatedBooks,
                        onBookClicked = onBookClicked
                    )
                }
            }
        }
}

@Composable
fun RecentlyUpdatedBooksCard(modifier: Modifier = Modifier, books: List<BookWithAuthor>, onBookClicked: (bookId: Int) -> Unit){
    ElevatedCard(modifier = modifier) {
        Text(
            text = stringResource(R.string.homescreen_recently_updated_books),
            fontSize = TextUnit(20F, TextUnitType.Sp),
            modifier = Modifier.padding(10.dp)
        )
        HorizontalDivider()
        RecentlyUpdatedBooks(
            books = books,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            onBookClicked = onBookClicked
        )
    }
}

@Composable
fun RecentlyUpdatedBooks(modifier: Modifier = Modifier, books: List<BookWithAuthor>, onBookClicked: (bookId: Int) -> Unit){
    Column(modifier) {
        books.forEach {book ->
            BookListCard(book) { onBookClicked(book.book.id) }
        }
    }
}

@Composable
fun BookReadStateStatsCard(modifier: Modifier = Modifier, totalBooks: Int, bookReadStateCount: Map<BookReadState, Int>){
    ElevatedCard(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Column {
                Box {
                    Text(
                        stringResource(R.string.homescreen_total_books, totalBooks),
                        fontSize = TextUnit(20F, TextUnitType.Sp),
                        fontWeight = FontWeight.Bold
                    )
                }
                HorizontalDivider()
                Column{
                    bookReadStateCount.forEach { (bookReadState, count) ->
                        Row(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)){
                            Text(
                                stringResource(
                                    R.string.homescreen_book_read_state_count,
                                    stringResource(bookReadState.displayNameRes),
                                    count
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}