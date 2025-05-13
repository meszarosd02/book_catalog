package com.meszarosd.book_catalog.navigation.topbar.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.meszarosd.book_catalog.R

@Composable
fun FilterActionIconButton(modifier: Modifier = Modifier, onIconClick: () -> Unit) {
    IconButton(
        onClick = {onIconClick()}
    ) {
        Icon(painterResource(R.drawable.baseline_filter_list_24), contentDescription = "Filter action")
    }
}