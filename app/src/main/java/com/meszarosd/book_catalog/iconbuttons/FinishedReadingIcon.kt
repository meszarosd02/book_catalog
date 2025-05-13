package com.meszarosd.book_catalog.iconbuttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FinishedReadingIcon(modifier: Modifier = Modifier) {
    Icon(
        Icons.Default.Check,
        contentDescription = "Finished Reading",
        modifier = modifier
    )
}