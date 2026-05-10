package com.core.designsystem.component

import androidx.compose.runtime.Composable
import com.core.designsystem.theme.AppTheme

@ThemePreviews
@Composable
fun SearchTopAppBarPreview() {
    AppTheme {
        SearchTopAppBar(
            query = "query",
            onQueryChange = {},
            onSearchClose = {},
            onSearchOpen = {},
            isSearching = true,
        )
    }
}
