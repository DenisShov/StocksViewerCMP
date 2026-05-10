package com.feature.list.impl.data.repository

import arrow.core.Either
import com.core.common.error.DomainError
import com.core.network.ktor.StocksApiService
import com.core.network.model.errors.ApiError
import com.core.network.model.stocksList.TickerResponse
import com.core.network.model.stocksList.TickersResponse
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StocksListRepositoryImplTest {

    private lateinit var repository: StocksListRepositoryImpl

    private lateinit var getStocksApi: StocksApiService

    @BeforeTest
    fun setUp() {
        getStocksApi = mock()
        repository = StocksListRepositoryImpl(stocksApi = getStocksApi)
    }

    @Test
    fun getStockList_returns_mapped_tickers_on_success() = runTest {
        everySuspend { getStocksApi.getStockList(null) } returns Either.Right(tickersResponse)

        val result = repository.getStockList(null)

        assertTrue(result.isRight())
        result.onRight {
            assertEquals(1, it.results.size)
            assertEquals("AAPL", it.results.first().ticker)
        }
    }

    @Test
    fun getStockList_returns_HttpError_on_api_http_error() = runTest {
        everySuspend { getStocksApi.getStockList(null) } returns
            Either.Left(ApiError.HttpError(404, "Not Found", null))

        val result = repository.getStockList(null)

        assertTrue(result.isLeft())
        result.onLeft {
            assertTrue(it is DomainError.HttpError)
            assertEquals(404, (it as DomainError.HttpError).code)
        }
    }

    @Test
    fun getStockList_returns_MissingNetworkConnection_on_network_error() = runTest {
        everySuspend { getStocksApi.getStockList(null) } returns
            Either.Left(ApiError.NetworkError(RuntimeException("No network")))

        val result = repository.getStockList(null)

        assertTrue(result.isLeft())
        result.onLeft { assertTrue(it is DomainError.MissingNetworkConnection) }
    }

    @Test
    fun searchStockByQuery_returns_mapped_tickers_on_success() = runTest {
        everySuspend { getStocksApi.searchStockByQuery("AAPL", null) } returns Either.Right(
            tickersResponse
        )

        val result = repository.searchStockByQuery("AAPL", null)

        assertTrue(result.isRight())
        result.onRight { assertEquals("AAPL", it.results.first().ticker) }
    }

    @Test
    fun searchStockByQuery_returns_error_on_api_failure() = runTest {
        everySuspend { getStocksApi.searchStockByQuery("AAPL", null) } returns
            Either.Left(ApiError.HttpError(500, "Server Error", null))

        val result = repository.searchStockByQuery("AAPL", null)

        assertTrue(result.isLeft())
        result.onLeft { assertTrue(it is DomainError.HttpError) }
    }

    private val tickerResponse = TickerResponse(
        ticker = "AAPL",
        name = "Apple Inc.",
        market = "stocks",
        locale = "us",
        primaryExchange = "XNAS",
        type = "CS",
        active = true,
    )

    private val tickersResponse = TickersResponse(
        results = listOf(tickerResponse),
        status = "OK",
        requestId = "req1",
        count = 1,
    )
}
