package com.feature.list.impl.ui.paging

import com.core.common.error.DomainError
import com.feature.list.impl.domain.model.Ticker
import com.feature.list.impl.domain.repository.StocksListRepository

interface StocksSearchPager {
    fun createPagingSource(query: String): FlowPagingSource<Ticker>
}

class StocksSearchPagerImpl(private val repository: StocksListRepository) : StocksSearchPager {

    override fun createPagingSource(query: String): FlowPagingSource<Ticker> =
        FlowPagingSource(pageSize = PAGE_SIZE) { cursor, _ ->
            val result = if (query.isNotEmpty()) {
                repository.searchStockByQuery(searchQuery = query, cursor = cursor)
            } else {
                repository.getStockList(cursor = cursor)
            }

            result.fold(
                ifLeft = { error -> throw mapToSearchResultError(error) },
                ifRight = { tickers ->
                    PageResult(
                        items = tickers.results,
                        nextCursor = tickers.nextUrl?.substringAfter(CURSOR_PARAMETER),
                    )
                }
            )
        }

    private fun mapToSearchResultError(error: DomainError): SearchResultsError = when (error) {
        is DomainError.MissingNetworkConnection -> SearchResultsError.NetworkError
        is DomainError.HttpError -> SearchResultsError.HttpError(errorMessage = error.message)
        else -> SearchResultsError.UnknownError
    }

    companion object {
        private const val CURSOR_PARAMETER = "cursor="
        private const val PAGE_SIZE = 20
    }
}
