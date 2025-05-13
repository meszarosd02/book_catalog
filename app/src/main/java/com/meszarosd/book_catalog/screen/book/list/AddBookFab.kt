package com.meszarosd.book_catalog.screen.book.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.meszarosd.book_catalog.navigation.NavigationUIState

@Composable
fun AddBookFab(
    navigationUIState: NavigationUIState,
    viewModel: BookListViewModel
){
    FloatingActionButton(
        onClick = {navigationUIState.navController.navigate("addbook")}
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add new book")
    }
}