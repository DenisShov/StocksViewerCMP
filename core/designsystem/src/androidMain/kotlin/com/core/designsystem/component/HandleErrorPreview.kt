package com.core.designsystem.component

import androidx.compose.runtime.Composable
import com.core.designsystem.theme.AppTheme

@BackgroundPreview
@Composable
fun HandleErrorPreview() {
    AppTheme {
        HandleError(
            errorMessage = "Unable to load data. Please check your internet connection and try again.",
            onRetry = {}
        )
    }
}
