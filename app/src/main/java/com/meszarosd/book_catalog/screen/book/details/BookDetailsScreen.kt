package com.meszarosd.book_catalog.screen.book.details

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meszarosd.book_catalog.R
import com.meszarosd.book_catalog.components.titleTextFontSize
import com.meszarosd.book_catalog.entities.BookReadState
import com.meszarosd.book_catalog.entities.BookWithAuthor
import com.meszarosd.book_catalog.iconbuttons.CurrentlyReadingIcon
import com.meszarosd.book_catalog.iconbuttons.FinishedReadingIcon
import com.meszarosd.book_catalog.iconbuttons.NotReadIcon
import com.meszarosd.book_catalog.navigation.NavigationUIState
import com.meszarosd.book_catalog.navigation.topbar.icons.NavigateBackIcon
import com.meszarosd.book_catalog.screen.BaseScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    bookId: Int,
    navigationUIState: NavigationUIState,
    onBookDelete: () -> Unit,
    viewModel: BookDetailsViewModel = hiltViewModel()
){
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(bookId) {
        viewModel.getBook(bookId)
    }

    val userRatingSheetState = rememberModalBottomSheetState()
    val yearReadSheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

    BaseScreen(
        title = state.book?.book?.title ?: "",
        navigationUIState = navigationUIState,
        navigationIcon = { NavigateBackIcon(navigationUIState.navController) },
        actions = { BookDetailsAction(
            userRatingAction = {
                scope.launch {
                    userRatingSheetState.show()
                }
            },
            yearReadAction = {
                scope.launch {
                    yearReadSheetState.show()
                }
            }
        ) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(20.dp).padding(padding)
        ) {
            if (state.book != null) {
                BookReadStateSegmentedButtons(state.book!!){brs, bookId ->
                    viewModel.updateBookReadState(brs, bookId)
                }
                BookDetailCard(state.book!!) {
                    viewModel.deleteBook()
                    onBookDelete()
                }
            }
            if(userRatingSheetState.isVisible){
                UserRatingBottomSheet(
                    scope = scope,
                    bottomSheetState = userRatingSheetState,
                    currentRating = viewModel.uiState.value.book?.book?.userRating ?: 0,
                    omitSetValue = {rating ->
                        viewModel.updateRating(bookId, rating)
                        scope.launch{
                            userRatingSheetState.hide()
                        }
                    }
                )
            }
            if(yearReadSheetState.isVisible){
                YearReadBottomSheet(
                    scope = scope,
                    bottomSheetState = yearReadSheetState,
                    currentYear = viewModel.uiState.value.book?.book?.yearRead ?: 0,
                    omitSetValue = {year ->
                        viewModel.updateYearRead(bookId, year)
                        scope.launch {
                            yearReadSheetState.hide()
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun BookDetailCardPreview(){
    val book = BookWithAuthor(0, "Teszt könyv", BookReadState.NOT_READ, 0, "Teszt Iro")

    BookDetailCard(book) { }
}

@Composable
fun BookDetailCard(
    book: BookWithAuthor,
    onBookDelete: () -> Unit
){
    ElevatedCard {
        Column {
            Box(modifier = Modifier.fillMaxWidth()){
                Text(
                    book.book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(20F, TextUnitType.Sp),
                    modifier = Modifier.align(Alignment.CenterStart).padding(10.dp)
                )
                val centerEnd: Modifier = Modifier.align(Alignment.CenterEnd).padding(10.dp)
                when(book.book.bookReadState){
                    BookReadState.NOT_READ -> NotReadIcon(centerEnd)
                    BookReadState.CURRENTLY_READING -> CurrentlyReadingIcon(centerEnd)
                    BookReadState.FINISHED_READING -> FinishedReadingIcon(centerEnd)
                }
            }
            HorizontalDivider()
            Column {
                Row {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            book.author.name,
                            modifier = Modifier.align(Alignment.CenterStart).padding(10.dp)
                        )
                    }
                }
                Row{
                    Box(modifier = Modifier.fillMaxWidth()){
                        Text(
                            book.genre.name,
                            modifier = Modifier.align(Alignment.CenterStart).padding(10.dp)
                        )
                    }
                }
                Row{
                    Box(modifier = Modifier.fillMaxWidth()){
                        Text(
                            stringResource(
                                R.string.bookdetailsscreen_user_rating, book.book.userRating.toFloat()/2),
                            modifier = Modifier.align(Alignment.CenterStart).padding(10.dp)
                        )
                    }
                }
                if(book.book.yearRead != null) {
                    Row {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                stringResource(
                                    R.string.bookdetailsscreen_year_finished,
                                    book.book.yearRead
                                ),
                                modifier = Modifier.align(Alignment.CenterStart).padding(10.dp)
                            )
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth()){
                Button(
                    onClick = {
                        onBookDelete()
                    },
                    modifier = Modifier.align(Alignment.CenterStart).padding(10.dp)
                ) {
                    Text(stringResource(R.string.bookdetailsscreen_delete_book_button))
                }
                Column(
                    modifier = Modifier.align(Alignment.CenterEnd)
                ){
                    Text(
                        stringResource(
                            R.string.bookdetailsscreen_created_at,
                            SimpleDateFormat(
                                stringResource(R.string.bookdetailsscreen_created_at_format),
                                java.util.Locale.getDefault()
                            ).format(book.book.createdAt)
                        ),
                        fontSize = TextUnit(8F, TextUnitType.Sp),
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 5.dp, top = 2.dp)
                    )
                    Text(
                        stringResource(
                            R.string.bookdetailsscreen_updated_at,
                            SimpleDateFormat(
                                stringResource(R.string.bookdetailsscreen_updated_at_format),
                                java.util.Locale.getDefault()
                            ).format(book.book.updatedAt)
                        ),
                        fontSize = TextUnit(8F, TextUnitType.Sp),
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 5.dp, top = 2.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRatingBottomSheet(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    bottomSheetState: SheetState,
    currentRating: Int,
    omitSetValue: (Int) -> Unit
){
    val sliderState = SliderState(
        value = (currentRating.toFloat()/2),
        valueRange = 0f..5f,
        steps = 9
    )
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            scope.launch {
                bottomSheetState.hide()
            }
        },
        sheetState = bottomSheetState
    ) {
        Box(modifier = Modifier.fillMaxWidth()){
            Column {
                Row(modifier = Modifier.padding(10.dp)) {
                    Text(
                        stringResource(R.string.bookdetailsscreen_user_rating_title),
                        fontSize = titleTextFontSize)
                }
                HorizontalDivider()
                Row(modifier = Modifier.padding(10.dp)) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Slider(
                            state = sliderState,
                            modifier = Modifier.fillMaxWidth(0.7F).align(Alignment.CenterStart)
                        )
                        Text(
                            text = stringResource(R.string.bookdetailsscreen_slider_user_rating, sliderState.value),
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.Center){
                    Button(
                        onClick = {
                            omitSetValue((sliderState.value * 2).toInt())
                            scope.launch{
                                bottomSheetState.hide()
                            }
                        }
                    ){
                        Text(stringResource(R.string.bookdetailsscreen_set_user_rating_button))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearReadBottomSheet(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    bottomSheetState: SheetState,
    currentYear: Int,
    omitSetValue: (Int) -> Unit
){
    var selectedYear: Int by remember{ mutableIntStateOf(currentYear) }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            scope.launch {
                bottomSheetState.hide()
            }
        },
        sheetState = bottomSheetState
    ) {
        Box(modifier = Modifier.fillMaxWidth()){
            Column {
                val keyboard = LocalSoftwareKeyboardController.current
                Row(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = stringResource(R.string.bookdetailsscreen_year_read_title),
                        fontSize = titleTextFontSize
                    )
                }
                HorizontalDivider()
                Row(modifier = Modifier.fillMaxWidth().padding(10.dp)){
                    OutlinedTextField(
                        value = selectedYear.toString(),
                        onValueChange = {selectedYear = (it.trim()).toInt()},
                        label = {Text(stringResource(R.string.bookdetailsscreen_year_read_textfield_year))},
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                omitSetValue(selectedYear)
                                keyboard?.hide()
                            }
                        )
                    )
                }
                Row(modifier = Modifier.fillMaxWidth().padding(10.dp)){
                    Button(
                        onClick = {
                            omitSetValue(selectedYear)
                            keyboard?.hide()
                        }
                    ) {
                        Text(stringResource(R.string.bookdetailsscreen_year_read_change_button))
                    }
                }
            }
        }
    }
}

@Composable
fun BookReadStateSegmentedButtons(book: BookWithAuthor, onItemClick: (brs: BookReadState, bookId: Int) -> Unit){
    var selectedIndex by remember { mutableIntStateOf(book.book.bookReadState.ordinal) }
    val options = BookReadState.entries.map { stringResource(it.displayNameRes) }

    Icons.Default

    SingleChoiceSegmentedButtonRow (
        modifier = Modifier.height(IntrinsicSize.Min).padding(bottom = 10.dp)
    ) {
        options.forEachIndexed {index, label ->
            SegmentedButton(
                selected = index == selectedIndex,
                onClick = {
                    selectedIndex = index
                    onItemClick(BookReadState.entries[index], book.book.id)
                          },
                label = {
                    Text(
                        text = label,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.8F)
                    )
                        },
                shape = SegmentedButtonDefaults.itemShape(
                    index, options.size
                ),
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}