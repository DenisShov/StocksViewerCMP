package com.feature.details.impl.data.repository

import arrow.core.Either
import com.core.common.error.DomainError
import com.core.network.ktor.StocksApiService
import com.core.network.model.errors.ApiError
import com.core.network.model.stocksDetails.CandleResponse
import com.core.network.model.stocksDetails.CompanyResponse
import com.core.network.model.stocksDetails.StockChartResponse
import com.core.network.model.stocksDetails.StockOverviewResponse
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StocksDetailsRepositoryImplTest {

    private lateinit var repository: StocksDetailsRepositoryImpl

    private lateinit var getStocksApi: StocksApiService

    @BeforeTest
    fun setUp() {
        getStocksApi = mock()
        repository = StocksDetailsRepositoryImpl(stocksApi = getStocksApi)
    }

    @Test
    fun getStockOverviewByTicker_returns_mapped_overview_on_success() = runTest {
        everySuspend { getStocksApi.getStockOverview("AAPL") } returns Either.Right(stockOverviewResponse)

        val result = repository.getStockOverviewByTicker("AAPL")

        assertTrue(result.isRight())
        result.onRight {
            assertEquals("AAPL", it.results.ticker)
            assertEquals("Apple Inc.", it.results.name)
        }
    }

    @Test
    fun getStockOverviewByTicker_returns_error_on_api_failure() = runTest {
        everySuspend { getStocksApi.getStockOverview("AAPL") } returns
            Either.Left(ApiError.UnknownError(RuntimeException("unexpected")))

        val result = repository.getStockOverviewByTicker("AAPL")

        assertTrue(result.isLeft())
        result.onLeft { assertTrue(it is DomainError.GeneralError) }
    }

    @Test
    fun getStockChartData_returns_mapped_chart_on_success() = runTest {
        everySuspend {
            getStocksApi.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")
        } returns Either.Right(stockChartResponse)

        val result = repository.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")

        assertTrue(result.isRight())
        result.onRight {
            assertEquals("AAPL", it.ticker)
            assertEquals(1, it.results.size)
            assertEquals(150.0, it.results.first().close)
        }
    }

    @Test
    fun getStockChartData_returns_error_on_api_failure() = runTest {
        everySuspend {
            getStocksApi.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")
        } returns Either.Left(ApiError.HttpError(429, "Rate limited", null))

        val result = repository.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")

        assertTrue(result.isLeft())
        result.onLeft {
            assertTrue(it is DomainError.HttpError)
            assertEquals(429, (it as DomainError.HttpError).code)
        }
    }

    private val companyResponse = CompanyResponse(
        ticker = "AAPL",
        name = "Apple Inc.",
        market = "stocks",
        locale = "us",
        primaryExchange = "XNAS",
        type = "CS",
        active = true,
    )

    private val stockOverviewResponse = StockOverviewResponse(
        requestId = "req1",
        results = companyResponse,
        status = "OK",
    )

    private val candleResponse = CandleResponse(
        volume = 1000.0,
        vwap = 149.5,
        open = 148.0,
        close = 150.0,
        high = 151.0,
        low = 147.0,
        timestampMs = 1704067200000,
        transactions = 500,
    )

    private val stockChartResponse = StockChartResponse(
        ticker = "AAPL",
        queryCount = 1,
        resultsCount = 1,
        adjusted = true,
        results = listOf(candleResponse),
        status = "OK",
        requestId = "req1",
        count = 1,
    )
}
