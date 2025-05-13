package com.meszarosd.book_catalog.screen.author.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meszarosd.book_catalog.entities.Author

@Composable
fun AuthorDetailsScreen(
    authorId: Int,
    onAuthorDelete: () -> Unit,
    viewModel: AuthorDetailsViewModel = hiltViewModel()
){
    val state by viewModel.uiState

    LaunchedEffect(authorId) {
        viewModel.getAuthor(authorId)
    }

    Column(
        modifier = Modifier.padding(20.dp)
    ){
        if(state.author == null) return
        AuthorDetailsCard(state, {
            viewModel.deleteAuthor()
            onAuthorDelete()
        })
    }
}

@Preview
@Composable
fun AuthorDetailsCardPreview(){
    val state = AuthorDetailsUiState(
        author = Author(1, "Teszt Iro"),
        bookCount = 5
    )

    AuthorDetailsCard(state, {})
}

@Composable
fun AuthorDetailsCard(state: AuthorDetailsUiState, onAuthorDelete: () -> Unit,  modifier: Modifier = Modifier) {
    ElevatedCard {
        Column {
            Box(modifier = Modifier.fillMaxWidth()){
                Text(
                    state.author!!.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(20F, TextUnitType.Sp),
                    modifier = Modifier.align(Alignment.CenterStart).padding(10.dp)
                )
            }
            HorizontalDivider()
            Box(modifier = Modifier.fillMaxWidth()){
                Text(
                    "${state.bookCount} book(s)",
                    modifier = Modifier.align(Alignment.CenterStart).padding(10.dp)
                )
            }
            HorizontalDivider()
            Box(modifier = Modifier.fillMaxWidth()){
                Button(
                    onClick = {onAuthorDelete()},
                    modifier = Modifier.align(Alignment.CenterStart).padding(10.dp)
                ){
                    Text("Delete author")
                }
            }
        }
    }
}