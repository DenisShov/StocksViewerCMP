package com.feature.list.impl.di

import com.core.navigation.Navigator
import com.feature.details.api.StocksDetailKey
import com.feature.list.api.StocksListKey
import com.feature.list.impl.data.repository.StocksListRepositoryImpl
import com.feature.list.impl.domain.repository.StocksListRepository
import com.feature.list.impl.ui.StocksListViewModel
import com.feature.list.impl.ui.compose.StocksListRoute
import com.feature.list.impl.ui.paging.StocksSearchPager
import com.feature.list.impl.ui.paging.StocksSearchPagerImpl
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val stocksListModule = module {
    factory<StocksListRepository> { StocksListRepositoryImpl(get()) }
    factory<StocksSearchPager> { StocksSearchPagerImpl(get()) }
    viewModel { StocksListViewModel(get()) }

    navigation<StocksListKey> {
        val navigator = get<Navigator>()
        StocksListRoute(
            onStockClick = { ticker -> navigator.navigate(StocksDetailKey(ticker)) },
        )
    }
}
