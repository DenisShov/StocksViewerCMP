package com.feature.favorites.impl.ui.mapper

import com.core.component.favorites.domain.model.FavoriteStock
import com.feature.favorites.impl.ui.state.FavoriteStockUiModel

fun FavoriteStock.toUiModel(): FavoriteStockUiModel =
    FavoriteStockUiModel(
        ticker = ticker,
        name = name,
        type = getType(),
    )

private fun FavoriteStock.getType(): String = when (type) {
    "CS" -> "Common Stock"
    "ETF" -> "Exchange Traded Fund"
    "ADRC" -> "Depositary Receipt"
    else -> type
}
