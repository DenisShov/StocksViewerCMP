package com.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.core.designsystem.theme.AppTheme

@com.core.designsystem.component.ThemePreviews
@Composable
fun TagsRowPreview() {
    AppTheme {
        TagsRow(modifier = Modifier, listOf("kotlin", "javascript", "python"))
    }
}
