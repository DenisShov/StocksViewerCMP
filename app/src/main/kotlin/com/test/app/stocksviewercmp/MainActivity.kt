package com.test.app.stocksviewercmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.core.designsystem.theme.AppTheme
import com.test.app.stocksviewercmp.ui.StockViewerCMPApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(darkTheme = isSystemInDarkTheme()) {
                StockViewerCMPApp()
            }
        }
    }
}
