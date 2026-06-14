package com.feature.details.impl.di

import com.core.navigation.Navigator
import com.feature.details.api.StocksDetailKey
import com.feature.details.impl.data.repository.StocksDetailsRepositoryImpl
import com.feature.details.impl.domain.repository.StocksDetailsRepository
import com.feature.details.impl.domain.usecase.GetStockChartDataUseCase
import com.feature.details.impl.ui.StockDetailsViewModel
import com.feature.details.impl.ui.compose.StockDetailsRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val stockDetailsModule = module {
    factory<StocksDetailsRepository> { StocksDetailsRepositoryImpl(get()) }
    factory { GetStockChartDataUseCase(get()) }
    viewModel { params ->
        StockDetailsViewModel(
            ticker = params.get(),
            stocksDetailsRepository = get(),
            getStockChartDataUseCase = get(),
            favoritesRepository = get(),
            errorMapper = get(),
        )
    }

    navigation<StocksDetailKey> { route ->
        val navigator = get<Navigator>()
        val viewModel = koinViewModel<StockDetailsViewModel>(key = route.stockTicker) {
            parametersOf(route.stockTicker)
        }
        StockDetailsRoute(
            viewModel = viewModel,
            onBackButtonClick = navigator::onBackClick,
        )
    }
}
