package com.test.app.stocksviewercmp.shared

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.core.designsystem.icon.IconResources
import com.feature.favorites.api.FavoritesListKey
import com.feature.list.api.StocksListKey

enum class TopLevelTab(
    val key: NavKey,
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
