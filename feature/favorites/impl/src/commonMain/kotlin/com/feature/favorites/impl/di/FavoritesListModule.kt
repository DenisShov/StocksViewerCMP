package com.feature.favorites.impl.di

import com.core.navigation.Navigator
import com.feature.details.api.StocksDetailKey
import com.feature.favorites.api.FavoritesListKey
import com.feature.favorites.impl.ui.FavoritesListViewModel
import com.feature.favorites.impl.ui.compose.FavoritesListRoute
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val favoritesListModule = module {
    viewModel { FavoritesListViewModel(get()) }

    navigation<FavoritesListKey> {
        val navigator = get<Navigator>()
        FavoritesListRoute(
            onStockClick = { ticker -> navigator.navigate(StocksDetailKey(ticker)) },
        )
    }
}
