package com.meszarosd.book_catalog.screen.book.details

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.meszarosd.book_catalog.entities.BookReadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsAction(
    modifier: Modifier = Modifier,
    userRatingAction: () -> Unit,
    yearReadAction: () -> Unit
) {
    var expanded by remember{ mutableStateOf(false) }

        Box {
            IconButton(
                onClick = {
                    expanded = !expanded
                }
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(
                        text = "Set user rating"
                    ) },
                    onClick = {
                        expanded = false
                        userRatingAction()
                    }
                )
                DropdownMenuItem(
                    text = { Text(
                        text = "Change year read"
                    ) },
                    onClick = {
                        expanded = false
                        yearReadAction()
                    }
                )
            }
        }
}