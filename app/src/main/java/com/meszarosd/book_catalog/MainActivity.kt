package com.meszarosd.book_catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.meszarosd.book_catalog.navigation.AppNavigation
import com.meszarosd.book_catalog.ui.theme.HiltTesztTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            HiltTesztTheme(dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}