package com.feature.details.impl.domain.usecase

import arrow.core.left
import arrow.core.right
import com.core.common.error.DomainError
import com.feature.details.impl.domain.model.Candle
import com.feature.details.impl.domain.model.StockChart
import com.feature.details.impl.domain.repository.StocksDetailsRepository
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Clock

class GetStockChartDataUseCaseTest {

    private val repository: StocksDetailsRepository = mock()
    private lateinit var underTest: GetStockChartDataUseCase

    @BeforeTest
    fun setup() {
        everySuspend {
            repository.getStockChartData(any(), any(), any(), any())
        } returns testStockChart.right()

        underTest = GetStockChartDataUseCase(stocksDetailsRepository = repository)
    }

    @Test
    fun launch_thenPassesCorrectTicker() = runTest {
        underTest.launch("AAPL", "week")

        verifySuspend {
            repository.getStockChartData(
                ticker = "AAPL",
                startDate = any(),
                endDate = any(),
                period = any(),
            )
        }
    }

    @Test
    fun launch_thenStartDateIsTwoYearsAgo() = runTest {
        var capturedStartDate: String? = null
        everySuspend {
            repository.getStockChartData(any(), any(), any(), any())
        } calls { args ->
            capturedStartDate = args.arg<String>(1)
            testStockChart.right()
        }

        underTest.launch("AAPL", "week")

        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val expectedStartDate = today.minus(2, DateTimeUnit.YEAR).toString()
        assertEquals(expectedStartDate, capturedStartDate)
    }

    @Test
    fun launch_thenEndDateIsToday() = runTest {
        var capturedEndDate: String? = null
        everySuspend {
            repository.getStockChartData(any(), any(), any(), any())
        } calls { args ->
            capturedEndDate = args.arg<String>(2)
            testStockChart.right()
        }

        underTest.launch("AAPL", "week")

        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val expectedEndDate = today.toString()
        assertEquals(expectedEndDate, capturedEndDate)
    }

    @Test
    fun launch_thenPassesCorrectPeriod() = runTest {
        underTest.launch("AAPL", "month")

        verifySuspend {
            repository.getStockChartData(
                ticker = any(),
                startDate = any(),
                endDate = any(),
                period = "month",
            )
        }
    }

    @Test
    fun launch_whenNetworkError_thenReturnsLeftWithError() = runTest {
        everySuspend {
            repository.getStockChartData(any(), any(), any(), any())
        } returns DomainError.MissingNetworkConnection.left()

        val result = underTest.launch("AAPL", "week")

        assertTrue(result.isLeft())
        result.onLeft { error ->
            assertIs<DomainError.MissingNetworkConnection>(error)
        }
    }

    @Test
    fun launch_whenGeneralError_thenReturnsLeftWithGeneralError() = runTest {
        val exception = RuntimeException("Unexpected failure")
        everySuspend {
            repository.getStockChartData(any(), any(), any(), any())
        } returns DomainError.GeneralError(exception).left()

        val result = underTest.launch("AAPL", "week")

        assertTrue(result.isLeft())
        result.onLeft { error ->
            assertIs<DomainError.GeneralError>(error)
            assertEquals("Unexpected failure", error.exception.message)
        }
    }

    private val testCandles = listOf(
        Candle(
            volume = 50_000_000.0,
            vwap = 185.5,
            open = 185.82,
            close = 184.8,
            high = 186.03,
            low = 184.21,
            timestampMs = 1699851600000,
            transactions = 500000,
        ),
        Candle(
            volume = 45_000_000.0,
            vwap = 187.0,
            open = 187.7,
            close = 187.44,
            high = 188.11,
            low = 186.3,
            timestampMs = 1699938000000,
            transactions = 450000,
        ),
    )

    private val testStockChart = StockChart(
        ticker = "AAPL",
        queryCount = 2,
        resultsCount = 2,
        adjusted = true,
        results = testCandles,
        status = "OK",
        requestId = "test-request-id",
        count = 2,
    )
}
