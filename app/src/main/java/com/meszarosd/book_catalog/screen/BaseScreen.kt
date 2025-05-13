package com.meszarosd.book_catalog.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.meszarosd.book_catalog.navigation.NavigationUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    title: String,
    navigationUIState: NavigationUIState,
    navigationIcon: @Composable ((NavigationUIState) -> Unit) = {},
    fab: @Composable (() -> Unit) = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(title)},
                actions = actions,
                navigationIcon = {navigationIcon(navigationUIState)}
            )
        },
        content = content,
        floatingActionButton = fab
    )
}