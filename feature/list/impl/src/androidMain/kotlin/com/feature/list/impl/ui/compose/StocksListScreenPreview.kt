package com.feature.list.impl.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.core.designsystem.component.BackgroundPreview
import com.core.designsystem.theme.AppTheme
import com.feature.list.impl.ui.model.TickerUiModel
import com.feature.list.impl.ui.paging.PagedData
import kotlinx.collections.immutable.persistentListOf

@BackgroundPreview
@Composable
private fun StocksListPreview() {
    AppTheme {
        val mockData = PagedData(
            items = persistentListOf(
                TickerUiModel("AAPL", "Apple Inc.", "Common Stock"),
                TickerUiModel("GOOGL", "Alphabet Inc.", "Common Stock"),
                TickerUiModel("MSFT", "Microsoft Corporation", "Common Stock"),
            ),
            isLoading = false,
            error = null,
            hasMore = true,
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp),
        ) {
            items(
                items = mockData.items,
                key = { it.ticker },
                contentType = { "stock" },
            ) { stock ->
                StockListItem(stockItem = stock)
            }
        }
    }
}

@BackgroundPreview
@Composable
private fun StocksListSkeletonPreview() {
    AppTheme {
        StocksListSkeleton()
    }
}
