package com.test.app.stocksviewercmp.shared

import androidx.compose.ui.graphics.vector.ImageVector
import com.core.designsystem.icon.IconResources
import com.feature.favorites.api.FavoritesListKey
import com.feature.list.api.StocksListKey

enum class TopLevelTab(
    val key: Any,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    STOCKS(
        key = StocksListKey,
        label = "Stocks",
        selectedIcon = IconResources.HomeFilled,
        unselectedIcon = IconResources.HomeOutline,
    ),
    FAVORITES(
        key = FavoritesListKey,
        label = "Favorites",
        selectedIcon = IconResources.StarFilled,
        unselectedIcon = IconResources.StarOutline,
    ),
}
