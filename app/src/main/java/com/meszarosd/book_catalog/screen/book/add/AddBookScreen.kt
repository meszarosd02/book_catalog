package com.meszarosd.book_catalog.screen.book.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meszarosd.book_catalog.R
import com.meszarosd.book_catalog.entities.Author
import com.meszarosd.book_catalog.entities.Genre
import com.meszarosd.book_catalog.navigation.NavigationUIState
import com.meszarosd.book_catalog.navigation.topbar.icons.DrawerOpenIcon
import com.meszarosd.book_catalog.screen.BaseScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    navigationUIState: NavigationUIState,
    viewModel: AddBookViewModel = hiltViewModel()
){
    LaunchedEffect(null) {
        viewModel.fetchGenres()
    }
    BaseScreen(
        title = stringResource(R.string.navigation_title_addbook),
        navigationUIState = navigationUIState,
        navigationIcon = { DrawerOpenIcon(navigationUIState.scope, navigationUIState.drawerState) }
    ) { padding ->
        val state by viewModel.uiState
        val focusManager = LocalFocusManager.current

        var authorExpanded by remember{ mutableStateOf(false) }
        var selectedAuthor by remember{ mutableStateOf<Author?>(null) }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(padding), horizontalArrangement = Arrangement.Center) {
            ElevatedCard(modifier = Modifier.fillMaxWidth(0.7F)) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(stringResource(R.string.addbookscreen_add_new_book))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = state.inputTitle,
                            onValueChange = { viewModel.changeInputTitle(it) },
                            label = { Text(stringResource(R.string.addbookscreen_book_title)) },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                capitalization = KeyboardCapitalization.Sentences
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = authorExpanded,
                            onExpandedChange = { authorExpanded = !authorExpanded }
                        ) {
                            OutlinedTextField(
                                value = state.inputAuthor,
                                onValueChange = {
                                    viewModel.changeInputAuthor(it)
                                    if (it.length > 2) {
                                        viewModel.searchAuthor(state.inputAuthor)
                                    }
                                    if (selectedAuthor != null) {
                                        selectedAuthor = null
                                    } },
                                    label = { Text(stringResource(R.string.addbookscreen_book_author)) },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        capitalization = KeyboardCapitalization.Words
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = authorExpanded,
                                    onDismissRequest = { authorExpanded = false }
                                ) {
                                    if (state.authorSearchResult.list.isEmpty()) return@ExposedDropdownMenu
                                    state.authorSearchResult.list.forEach { author ->
                                        DropdownMenuItem(
                                            text = { Text(author.name) },
                                            onClick = {
                                                viewModel.changeInputAuthor(author.name)
                                                authorExpanded = false
                                                selectedAuthor = author
                                                focusManager.clearFocus()
                                                viewModel.clearAuthorSearchResult()
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            horizontalArrangement = Arrangement.Center
                        ){
                            GenreSelector(genres = state.genres){ genre ->
                                viewModel.changeSelectedGenre(genre)
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    viewModel.insertBook(selectedAuthor)
                                    focusManager.clearFocus()
                                }
                            ) {
                                Text(stringResource(R.string.addbookscreen_create), fontSize = TextUnit(14F, TextUnitType.Sp))
                            }
                        }
                    }
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreSelector(modifier: Modifier = Modifier, genres: List<Genre>, onGenreClicked: (Genre) -> Unit) {
    var expanded by remember {mutableStateOf(false)}
    var selectedGenre by remember { mutableStateOf<Genre?>(null) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded},
        modifier = modifier
    ) {
        TextField(
            value = selectedGenre?.name ?: "",
            readOnly = true,
            onValueChange = {},
            label = { Text(stringResource(R.string.addbookscreen_book_genre)) },
            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded)},
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false}
        ) {
            genres.forEach { genre ->
                DropdownMenuItem(
                    text = {Text(genre.name)},
                    onClick = {
                        expanded = false
                        selectedGenre = genre
                        onGenreClicked(genre)
                    }
                )
            }
        }
    }
}