package com.meszarosd.book_catalog.iconbuttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CurrentlyReadingIcon(modifier: Modifier = Modifier) {
    Icon(
        Icons.Default.PlayArrow,
        contentDescription = "Currently reading",
        modifier = modifier
    )
}