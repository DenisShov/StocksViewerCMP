package com.core.component.favorites.domain.repository

import com.core.component.favorites.domain.model.FavoriteStock
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getAllFavorites(): Flow<List<FavoriteStock>>
    fun isFavorite(ticker: String): Flow<Boolean>
    suspend fun addFavorite(stock: FavoriteStock)
    suspend fun removeFavorite(ticker: String)
}
