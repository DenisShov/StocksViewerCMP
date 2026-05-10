package com.core.ui

import androidx.compose.runtime.Composable
import com.core.designsystem.theme.AppTheme

@com.core.designsystem.component.ThemePreviews
@Composable
fun ErrorRetryItemPreview() {
    AppTheme {
        ErrorRetryItem(error = null) {}
    }
}
