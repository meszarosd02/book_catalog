package com.meszarosd.book_catalog.iconbuttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NotReadIcon(modifier: Modifier = Modifier){
    Icon(
        Icons.Outlined.Close,
        contentDescription = "Not read",
        modifier = modifier,

    )
}