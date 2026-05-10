package com.feature.favorites.impl.ui.compose

import androidx.compose.runtime.Composable
import com.core.designsystem.component.BackgroundPreview
import com.core.designsystem.theme.AppTheme
import com.feature.favorites.impl.ui.state.FavoriteStockUiModel

@BackgroundPreview
@Composable
fun FavoriteStockItemPreview() {
    AppTheme {
        FavoriteStockItem(
            FavoriteStockUiModel(
                ticker = "A",
                name = "Agilent Technologies Inc.",
                type = "Exchange Traded Fund",
            ),
            onClick = {}
        )
    }
}
