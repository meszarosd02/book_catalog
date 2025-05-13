package com.meszarosd.book_catalog.screen.author.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meszarosd.book_catalog.entities.Author

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorListScreen(
    onAuthorClicked: (Author) -> Unit,
    viewModel: AuthorListViewModel = hiltViewModel()
){
    val state by viewModel.uiState

    LaunchedEffect(null) {
        viewModel.fetchAuthors {  }
    }

    Column(modifier = Modifier.padding(20.dp)){
        var isRefreshing by remember{ mutableStateOf(false) }
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                viewModel.fetchAuthors(){
                    isRefreshing = false
                }
            }
        ) {
            LazyColumn {
                items(state.authors) { author ->
                    AuthorListCard(author, onAuthorClicked)
                }
            }
        }
    }
}

@Preview
@Composable
fun AuthorListCardPreview(){
    val author = Author(0, "Gipsz Jakab")

    AuthorListCard(author){}
}

@Composable
fun AuthorListCard(
    author: Author,
    onAuthorClicked: (Author) -> Unit
){
    Column(
        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            .clickable(onClick = {onAuthorClicked(author)})
    ) {
        ElevatedCard {
            Column {
                Box(modifier = Modifier.fillMaxWidth()){
                    Text(
                        author.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(20F, TextUnitType.Sp),
                        modifier = Modifier.align(Alignment.CenterStart).padding(10.dp)
                    )
                }
                HorizontalDivider()
            }
        }
    }
}