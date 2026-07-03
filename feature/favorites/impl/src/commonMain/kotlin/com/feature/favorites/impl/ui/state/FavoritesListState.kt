package com.feature.favorites.impl.ui.state

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

sealed interface FavoritesListState {
    data object Loading : FavoritesListState
    data object Empty : FavoritesListState

    @Immutable
    data class Content(val favorites: ImmutableList<FavoriteStockUiModel>) : FavoritesListState
}

@Immutable
data class FavoriteStockUiModel(
    val ticker: String,
    val name: String,
    val type: String,
)
