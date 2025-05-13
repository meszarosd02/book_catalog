package com.meszarosd.book_catalog.navigation.topbar.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun NavigateBackIcon(navController: NavHostController){
    IconButton(
        onClick = {
            navController.navigateUp()
        }
    ){
        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Navigate back")
    }
}