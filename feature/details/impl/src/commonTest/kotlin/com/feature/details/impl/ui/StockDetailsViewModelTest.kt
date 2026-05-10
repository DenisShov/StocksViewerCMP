package com.feature.details.impl.ui

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.core.common.error.DomainError
import com.core.common.mapper.ErrorMapper
import com.feature.details.impl.domain.model.Candle
import com.feature.details.impl.domain.model.Company
import com.feature.details.impl.domain.model.StockChart
import com.feature.details.impl.domain.model.StockOverview
import com.feature.details.impl.domain.repository.StocksDetailsRepository
import com.feature.details.impl.domain.usecase.GetStockChartDataUseCase
import com.feature.details.impl.ui.actions.ChartPeriod
import com.sharedlibrary.favorites.domain.repository.FavoritesRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class StockDetailsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val stocksDetailsRepository: StocksDetailsRepository = mock()
    private val getStockChartDataUseCase: GetStockChartDataUseCase = mock()
    private val favoritesRepository: FavoritesRepository = mock()
    private val errorMapper: ErrorMapper = mock()
    private lateinit var sut: StockDetailsViewModel

    private fun createSut() = StockDetailsViewModel(
        ticker = "AAPL",
        stocksDetailsRepository = stocksDetailsRepository,
        getStockChartDataUseCase = getStockChartDataUseCase,
        favoritesRepository = favoritesRepository,
        errorMapper = errorMapper,
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        everySuspend { stocksDetailsRepository.getStockOverviewByTicker(any()) } returns testStockDetails.right()
        everySuspend { getStockChartDataUseCase.launch(any(), any()) } returns testStockChart.right()
        every { errorMapper.mapToStringError(any()) } returns "Something went wrong"
        every { favoritesRepository.isFavorite(any()) } returns flowOf(false)
        sut = createSut()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun whenOverviewLoads_thenShowsStockOverview() = runTest {
        sut.uiState.test {
            val state = awaitItem()
            assertNotNull(state.stockOverview)
            assertEquals("AAPL", state.stockOverview?.ticker)
            assertEquals(false, state.isLoading)
            assertNull(state.errorString)
        }
    }

    @Test
    fun whenOverviewLoads_thenAlsoLoadsChartData() = runTest {
        sut.uiState.test {
            val state = awaitItem()
            assertTrue(state.candles.isNotEmpty())
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun whenOverviewFails_thenShowsError() = runTest {
        everySuspend {
            stocksDetailsRepository.getStockOverviewByTicker(any())
        } returns DomainError.MissingNetworkConnection.left()
        every {
            errorMapper.mapToStringError(DomainError.MissingNetworkConnection)
        } returns "No network connection"

        sut = createSut()

        sut.uiState.test {
            val state = awaitItem()
            assertNull(state.stockOverview)
            assertEquals("No network connection", state.errorString)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun whenChartPeriodChanges_thenUpdatesCandles() = runTest {
        sut.uiState.test {
            expectMostRecentItem()

            sut.getStockChartData(ChartPeriod.MONTH)

            val state = expectMostRecentItem()
            assertTrue(state.candles.isNotEmpty())
            assertEquals(ChartPeriod.MONTH, state.selectedPeriod)
            assertEquals(false, state.isChartLoading)
        }
    }

    @Test
    fun whenChartDataFails_thenShowsError() = runTest {
        val httpError = DomainError.HttpError(500, "Internal Server Error")
        everySuspend {
            getStockChartDataUseCase.launch(any(), any())
        } returns httpError.left()
        every {
            errorMapper.mapToStringError(httpError)
        } returns "Internal Server Error"

        sut = createSut()

        sut.uiState.test {
            val state = awaitItem()
            // Overview succeeded but chart failed
            assertNotNull(state.stockOverview)
            assertEquals("Internal Server Error", state.chartErrorString)
        }
    }

    @Test
    fun whenRetry_thenReloadsData() = runTest {
        sut.uiState.test {
            val initialState = expectMostRecentItem()
            assertNotNull(initialState.stockOverview)

            sut.getStockOverviewByTicker()

            val state = expectMostRecentItem()
            assertNotNull(state.stockOverview)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun whenRetryChartData_thenUsesCurrentSelectedPeriod() = runTest {
        sut.uiState.test {
            expectMostRecentItem()

            sut.getStockChartData(ChartPeriod.MONTH)
            expectMostRecentItem()

            sut.retryGetStockChartData()

            val state = expectMostRecentItem()
            assertEquals(ChartPeriod.MONTH, state.selectedPeriod)
            assertTrue(state.candles.isNotEmpty())
            assertNull(state.chartErrorString)
            assertEquals(false, state.isChartLoading)
        }
    }

    @Test
    fun whenRetryChartDataAfterError_thenClearsChartError() = runTest {
        everySuspend { getStockChartDataUseCase.launch(any(), any()) } returns DomainError.MissingNetworkConnection.left()
        sut = createSut()

        sut.uiState.test {
            expectMostRecentItem() // error state after initial load

            everySuspend { getStockChartDataUseCase.launch(any(), any()) } returns testStockChart.right()
            sut.retryGetStockChartData()

            val state = expectMostRecentItem()
            assertTrue(state.candles.isNotEmpty())
            assertNull(state.chartErrorString)
            assertEquals(false, state.isChartLoading)
        }
    }

    private val testStockDetails = StockOverview(
        requestId = "test-request-id",
        results = Company(
            ticker = "AAPL",
            name = "Apple Inc.",
            market = "stocks",
            locale = "us",
            primaryExchange = "XNAS",
            type = "CS",
            active = true,
            currencyName = "usd",
            marketCap = 3_000_000_000_000.0,
            totalEmployees = 164000,
            description = "Apple Inc. designs, manufactures, and markets smartphones and personal computers.",
            sicDescription = "Electronic Computers",
        ),
        status = "OK",
    )

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
