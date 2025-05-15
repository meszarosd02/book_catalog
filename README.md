# Book Catalog
Reference work Android application in Kotlin using Jetpack Compose and Hilt.

## Features
 - Add any book by entering the book title, author name, and genre.
 - Choose between 10 different default genres.
 - Set a personal rating to any book.
 - Choose if you the book isn't started yet, currently reading, or finished reading.
 - Set which year the book has been read (default is the current year).
 - Get statistics on the home screen (total books, recently updated books).

## Techs used

 - Room Database with migrations
 - State management using StateFlow
 - LazyColumn, FlowRow
 - DropDownMenu
 - Generalized navigation
 - A BaseScreen which implements TopAppBar (with Navigation Icon, and Actions)
 - Filtering using Chips
 - Generalized sorting using generics
 - UI State -> ViewModel -> Composable Screen architecture
 - Floating Action Button
 - Modal Bottom Sheet
