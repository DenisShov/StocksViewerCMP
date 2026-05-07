package com.test.app.stocksviewercmp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.test.app.stocksviewercmp.navigation.NavigationRoot

@Composable
fun StockViewerCMPApp(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        NavigationRoot()
    }
}
