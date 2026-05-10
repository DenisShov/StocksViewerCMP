package com.feature.list.impl.di

import com.feature.list.impl.data.repository.StocksListRepositoryImpl
import com.feature.list.impl.domain.repository.StocksListRepository
import com.feature.list.impl.ui.StocksListViewModel
import com.feature.list.impl.ui.paging.StocksSearchPager
import com.feature.list.impl.ui.paging.StocksSearchPagerImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val stocksListModule = module {
    factory<StocksListRepository> { StocksListRepositoryImpl(get()) }
    factory<StocksSearchPager> { StocksSearchPagerImpl(get()) }
    viewModel { StocksListViewModel(get()) }
}
