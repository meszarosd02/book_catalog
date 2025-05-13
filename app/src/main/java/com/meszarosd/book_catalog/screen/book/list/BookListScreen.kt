package com.meszarosd.book_catalog.screen.book.list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meszarosd.book_catalog.R
import com.meszarosd.book_catalog.components.titleTextFontSize
import com.meszarosd.book_catalog.entities.BookReadState
import com.meszarosd.book_catalog.entities.BookWithAuthor
import com.meszarosd.book_catalog.entities.Genre
import com.meszarosd.book_catalog.iconbuttons.CurrentlyReadingIcon
import com.meszarosd.book_catalog.iconbuttons.FinishedReadingIcon
import com.meszarosd.book_catalog.iconbuttons.NotReadIcon
import com.meszarosd.book_catalog.navigation.NavigationUIState
import com.meszarosd.book_catalog.navigation.topbar.icons.DrawerOpenIcon
import com.meszarosd.book_catalog.navigation.topbar.icons.FilterActionIconButton
import com.meszarosd.book_catalog.navigation.topbar.icons.SortActionIconButton
import com.meszarosd.book_catalog.screen.BaseScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    onBookClicked: (bookId: Int) -> Unit,
    navigationUIState: NavigationUIState,
    viewModel: BookListViewModel = hiltViewModel()
){
    val state by viewModel.uiState.collectAsState()
    val selectedGenres by viewModel.selectedGenres.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(null) {
        if(state.filteredBooks.isEmpty()) {
            viewModel.fetchBooks()
            viewModel.fetchGenres()
        }
        Log.d("BookListScreen", selectedGenres.toString())
    }
    BaseScreen(
        title = stringResource(R.string.navigation_title_booklist),
        navigationUIState = navigationUIState,
        navigationIcon = { DrawerOpenIcon(navigationUIState.scope, navigationUIState.drawerState) },
        actions = {
            SortActionIconButton(onIconClick = {})
            FilterActionIconButton(onIconClick = {
            scope.launch{
                bottomSheetState.show()
            }
        }
        )

                  },
        fab = { AddBookFab(navigationUIState, viewModel) }
    ) { padding ->
        Column(modifier = Modifier
            .padding(20.dp)
            .padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SortBookListDropDown(viewModel)
            }
            HorizontalDivider(modifier = Modifier.padding(top = 10.dp))
            BookListWithRefreshBox(
                books = state.filteredBooks,
                onRefresh = { viewModel.fetchBooks() },
                onBookClicked = onBookClicked
            )
            if(bottomSheetState.isVisible)
                FilterBottomSheet(
                    bottomSheetState = bottomSheetState,
                    scope = scope,
                    filterItems = state.genres,
                    activeGenres = selectedGenres,
                    onChipClick = {genre -> viewModel.toggleGenreFilter(genre)}
                )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    modifier: Modifier = Modifier,
    bottomSheetState: SheetState,
    scope: CoroutineScope,
    filterItems: List<Genre>,
    activeGenres: Set<Genre>,
    onChipClick: (Genre) -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            scope.launch {
                bottomSheetState.hide()
            }
        },
        sheetState = bottomSheetState
    ) {
        Box(
            modifier = Modifier.padding(10.dp).fillMaxWidth()
        ){
            Column{
                Row{
                    Text(
                        text = stringResource(R.string.booklistscreen_filter_genre),
                        fontSize = titleTextFontSize
                    )
                }
                FlowRow {
                    filterItems.forEach{ genre ->
                        BookListFilterChip(
                            genre = genre,
                            activeGenres = activeGenres
                        ) {
                            onChipClick(genre)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookListFilterChip(genre: Genre, activeGenres: Set<Genre>, onChipClick: () -> Unit){
    FilterChip(
        selected = genre in activeGenres,
        label = {Text(genre.name)},
        onClick = {
            onChipClick()
                  }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBookListDropDown(viewModel: BookListViewModel){
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(viewModel.sortOptions[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedOption.identifier,
            readOnly = true,
            onValueChange = {},
            label = { Text(stringResource(R.string.booklistscreen_sort_by)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            viewModel.sortOptions.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt.identifier) },
                    onClick = {
                        selectedOption = opt
                        expanded = false
                        viewModel.applySort(selectedOption, viewModel.isAscendingSorting.value)
                    }
                )
            }
        }
    }
    IconButton(
        onClick = {
            viewModel.isAscendingSorting.value = !viewModel.isAscendingSorting.value
            viewModel.applySort(selectedOption, viewModel.isAscendingSorting.value)
        }
    ) {
        Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = stringResource(R.string.booklistscreen_icon_sort_direction),
            modifier = Modifier.rotate(if (viewModel.isAscendingSorting.value) 180f else 0f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListWithRefreshBox(
    books: List<BookWithAuthor>,
    onRefresh: suspend () -> Unit,
    onBookClicked: (bookId: Int) -> Unit
){
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                onRefresh()
                isRefreshing = false
            }
        }
    ) {
        LazyColumn() {
            items(books) { book ->
                BookListCard(book, onBookClicked)
            }
        }
    }
}

@Preview
@Composable
fun BookListCardPreview(){
    val book = BookWithAuthor(0, "Teszt könyv", BookReadState.NOT_READ, 1, "Gipsz Jakab")
    BookListCard(book) { }
}

@Composable
fun BookListCard(
    book: BookWithAuthor,
    onBookClicked: (bookId: Int) -> Unit
){
    Column(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .clickable(onClick = { onBookClicked(book.book.id) })
    ) {
        ElevatedCard {
            Column {
                Box(modifier = Modifier.fillMaxWidth()){
                    Text(
                        book.book.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = titleTextFontSize,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(10.dp)
                    )
                    when(book.book.bookReadState){
                        BookReadState.NOT_READ -> NotReadIcon(
                            Modifier
                                .align(Alignment.CenterEnd)
                                .padding(10.dp))
                        BookReadState.CURRENTLY_READING -> CurrentlyReadingIcon(
                            Modifier
                                .align(Alignment.CenterEnd)
                                .padding(10.dp))
                        BookReadState.FINISHED_READING -> FinishedReadingIcon(
                            Modifier
                                .align(Alignment.CenterEnd)
                                .padding(10.dp))
                    }
                }
                HorizontalDivider()
                Box(modifier = Modifier.fillMaxWidth()){
                    Text(
                        book.author.name,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(10.dp)
                    )
                }
            }
        }
    }
}