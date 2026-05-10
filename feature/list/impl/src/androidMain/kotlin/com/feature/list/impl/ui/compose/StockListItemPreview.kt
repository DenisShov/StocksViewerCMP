package com.feature.list.impl.ui.compose

import androidx.compose.runtime.Composable
import com.core.designsystem.component.BackgroundPreview
import com.core.designsystem.theme.AppTheme
import com.feature.list.impl.ui.model.TickerUiModel

@BackgroundPreview
@Composable
fun StockListItemPreview() {
    AppTheme {
        StockListItem(
            TickerUiModel(
                ticker = "A",
                name = "Agilent Technologies Inc.",
                type = "Exchange Traded Fund",
            )
        )
    }
}
