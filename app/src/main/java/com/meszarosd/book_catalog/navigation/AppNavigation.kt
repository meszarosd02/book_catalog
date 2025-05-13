package com.meszarosd.book_catalog.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.meszarosd.book_catalog.screen.about.AboutScreen
import com.meszarosd.book_catalog.screen.author.details.AuthorDetailsScreen
import com.meszarosd.book_catalog.screen.author.list.AuthorListScreen
import com.meszarosd.book_catalog.screen.book.add.AddBookScreen
import com.meszarosd.book_catalog.screen.book.details.BookDetailsScreen
import com.meszarosd.book_catalog.screen.book.list.BookListScreen
import com.meszarosd.book_catalog.screen.home.HomeScreen
import com.meszarosd.book_catalog.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class NavigationUIState(
    val navController: NavHostController,
    val drawerState: DrawerState,
    val scope: CoroutineScope
)

sealed class Screen(
    val route: String,
    @StringRes val title: Int
){
    data object Home : Screen("home", R.string.navigation_title_home)
    data object BookList : Screen("booklist", R.string.navigation_title_booklist)
    data object BookDetails : Screen(
        "bookdetails/{bookId}", R.string.navigation_title_bookdetails
    ){
        fun createRoute(bookId: Int) = "bookdetails/${bookId}"
    }
    data object AuthorList : Screen("authorlist", R.string.navigation_title_authorlist)
    data object AuthorDetails : Screen("authordetails/{authorId}", R.string.navigation_title_authordetails){
        fun createRoute(authorId: Int) = "authordetails/${authorId}"
    }
    data object AddBook : Screen("addbook", R.string.navigation_title_addbook)
    data object About: Screen("about", R.string.navigation_title_about)
}

@Composable
fun NavGraph(navigationUIState: NavigationUIState, modifier: Modifier = Modifier){
    NavHost(navigationUIState.navController, startDestination = Screen.Home.route, modifier = modifier){
        composable(Screen.Home.route){
            HomeScreen(navigationUIState, onBookClicked = { bookId ->
                navigationUIState.navController.navigate(Screen.BookDetails.createRoute(bookId))
            })
        }
        composable(Screen.BookList.route){
            BookListScreen(onBookClicked = {bookId ->
                navigationUIState.navController.navigate(Screen.BookDetails.createRoute(bookId)){
                    popUpTo(Screen.BookList.route) {saveState = true}
                    restoreState = true
                }
            },
                navigationUIState = navigationUIState)
        }
        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(navArgument("bookId") {type = NavType.IntType})
        ){
            val bookId = it.arguments?.getInt("bookId") ?: return@composable
            BookDetailsScreen(bookId, onBookDelete = {
                navigationUIState.navController.navigateUp()
            },
                navigationUIState = navigationUIState)
        }
        composable(Screen.AuthorList.route){
            AuthorListScreen(onAuthorClicked = {author ->
                navigationUIState.navController.navigate(Screen.AuthorDetails.createRoute(author.id))
            })
        }
        composable(
            route = Screen.AuthorDetails.route,
            arguments = listOf(navArgument("authorId"){type = NavType.IntType})
        ){
            val authorId = it.arguments?.getInt("authorId") ?: return@composable
            AuthorDetailsScreen(authorId, onAuthorDelete = {
                navigationUIState.navController.navigateUp()
            })
        }
        composable(
            route = Screen.AddBook.route
        ){
            AddBookScreen(navigationUIState)
        }
        composable(
            route = Screen.About.route
        ){
            AboutScreen(navigationUIState)
        }
    }
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navigationUIState = NavigationUIState(navController, drawerState, scope)

    @Composable
    fun DrawerNavigationItem(screen: Screen){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    scope.launch {
                        navController.navigate(screen.route)
                        drawerState.close()
                    }
                }
                .padding(10.dp),
            horizontalArrangement = Arrangement.Start,
        ){
            Text(
                stringResource(screen.title),
                fontSize = TextUnit(20F, TextUnitType.Sp)
            )
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.statusBarsPadding(),
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Navigation",
                    fontSize = TextUnit(26F, TextUnitType.Sp)
                )
                HorizontalDivider()
                DrawerNavigationItem(Screen.Home)
                HorizontalDivider()
                DrawerNavigationItem(Screen.AddBook)
                HorizontalDivider()
                DrawerNavigationItem(Screen.BookList)
                DrawerNavigationItem(Screen.AuthorList)
                HorizontalDivider()
                DrawerNavigationItem(Screen.About)
            }
        }
    ) {
        NavGraph(navigationUIState, modifier = Modifier
            .fillMaxSize())
    }
}

