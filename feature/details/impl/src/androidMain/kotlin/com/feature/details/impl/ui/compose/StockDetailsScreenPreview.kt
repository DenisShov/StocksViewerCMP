package com.feature.details.impl.ui.compose

import androidx.compose.runtime.Composable
import com.core.designsystem.component.BackgroundPreview
import com.core.designsystem.theme.AppTheme
import com.feature.details.impl.ui.actions.ChartPeriod
import com.feature.details.impl.ui.actions.StockDetailsActions
import com.feature.details.impl.ui.model.CandleUiModel
import com.feature.details.impl.ui.model.StockOverviewUiModel
import com.feature.details.impl.ui.state.StockDetailsState

@BackgroundPreview
@Composable
private fun StockDetailsContentPreview() {
    AppTheme {
        StockDetailScreen(
            uiState = StockDetailsState(
                stockOverview = StockOverviewUiModel(
                    ticker = "ADBE",
                    name = "Adobe Inc.",
                    exchange = "NASDAQ",
                    marketCap = "110.59B",
                    totalEmployees = 31360L,
                    type = "Common Stock",
                    sicDescription = "Technology",
                    description = "Adobe provides content creation, document management, and digital marketing software.",
                    address = "345 Park Ave, San Jose, CA",
                    homepageUrl = "https://www.adobe.com",
                    listDate = "20 August 1986",
                    cik = "0000796343"
                ),
                candles = listOf(
                    CandleUiModel(open = 185.82, close = 184.8, high = 186.03, low = 184.21, timestampMs = 1699851600000),
                    CandleUiModel(open = 187.7, close = 187.44, high = 188.11, low = 186.3, timestampMs = 1699938000000),
                    CandleUiModel(open = 187.845, close = 188.01, high = 189.5, low = 187.78, timestampMs = 1700024400000),
                    CandleUiModel(open = 189.57, close = 189.71, high = 190.96, low = 188.65, timestampMs = 1700110800000),
                    CandleUiModel(open = 190.25, close = 189.69, high = 190.38, low = 188.57, timestampMs = 1700197200000),
                ),
                selectedPeriod = ChartPeriod.WEEK,
                isChartLoading = false,
                chartErrorString = null,
            ),
            onBackButtonClick = {},
            actions = StockDetailsActions(),
        )
    }
}

@BackgroundPreview
@Composable
private fun StockDetailsSkeletonPreview() {
    AppTheme {
        StockDetailsSkeleton()
    }
}
