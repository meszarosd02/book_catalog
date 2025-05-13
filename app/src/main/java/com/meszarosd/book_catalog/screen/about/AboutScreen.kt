package com.meszarosd.book_catalog.screen.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.meszarosd.book_catalog.R
import com.meszarosd.book_catalog.navigation.NavigationUIState
import com.meszarosd.book_catalog.navigation.topbar.icons.DrawerOpenIcon
import com.meszarosd.book_catalog.screen.BaseScreen

@Composable
fun AboutScreen(navigationUIState: NavigationUIState){
    BaseScreen(
        title = stringResource(R.string.navigation_title_about),
        navigationUIState = navigationUIState,
        navigationIcon = { DrawerOpenIcon(navigationUIState.scope, navigationUIState.drawerState) }
    ) { padding ->
        ElevatedCard(
            modifier = Modifier.padding(padding)
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        ) {
            Box(modifier = Modifier.padding(10.dp).fillMaxSize()){
                Column{
                    Row{
                       Text("Book Catalog", fontSize = TextUnit(22F, TextUnitType.Sp))
                    }
                    HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
                    Row{
                        Text("""Create a catalog of your books!
                            |Add your books by entering the title and author and select from 10 default genres!
                            |You are currently reading a book you've added? You can also set it!
                            """.trimMargin())
                    }
                }
                Row(modifier = Modifier.align(Alignment.BottomCenter)){
                    Text("Copyright © 2025 Dominik Mészáros\nAll rights reserved.", textAlign = TextAlign.Center)
                }
            }
        }
    }
}