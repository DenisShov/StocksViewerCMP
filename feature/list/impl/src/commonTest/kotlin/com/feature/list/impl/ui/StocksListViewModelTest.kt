package com.feature.list.impl.ui

import app.cash.turbine.test
import com.feature.list.impl.domain.model.Ticker
import com.feature.list.impl.ui.paging.FlowPagingSource
import com.feature.list.impl.ui.paging.PageResult
import com.feature.list.impl.ui.paging.StocksSearchPager
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class StocksListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val stocksSearchPager: StocksSearchPager = mock()
    private lateinit var underTest: StocksListViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        every { stocksSearchPager.createPagingSource(any()) } returns
            FlowPagingSource(pageSize = 20) { _, _ ->
                PageResult(items = testStockOverviewLists, nextCursor = null)
            }

        underTest = StocksListViewModel(stocksSearchPager)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun whenInitialized_thenEmitsPagingData() = runTest {
        underTest.stocksPagingState.test {
            advanceTimeBy(1100)
            val state = expectMostRecentItem()
            assertNotNull(state)
            assertFalse(state.isLoading)
            assertTrue(state.items.isNotEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun whenInitialized_thenCallsPagerWithEmptyQuery() = runTest {
        underTest.stocksPagingState.test {
            advanceTimeBy(1100)
            expectMostRecentItem()
            cancelAndIgnoreRemainingEvents()
        }

        verify { stocksSearchPager.createPagingSource("") }
    }

    @Test
    fun whenSearchQueryChanges_thenCallsPagerWithQuery() = runTest {
        val searchResults = listOf(
            Ticker(
                ticker = "GOOGL",
                name = "Alphabet Inc.",
                market = "stocks",
                locale = "us",
                primaryExchange = "XNAS",
                type = "CS",
                active = true,
            ),
        )
        every { stocksSearchPager.createPagingSource("GOOGL") } returns
            FlowPagingSource(pageSize = 20) { _, _ ->
                PageResult(items = searchResults, nextCursor = null)
            }

        underTest.stocksPagingState.test {
            advanceTimeBy(1100)
            expectMostRecentItem()

            underTest.onSearchQueryChange("GOOGL")
            advanceTimeBy(1100)

            val state = expectMostRecentItem()
            assertNotNull(state)
            assertEquals(1, state.items.size)
            assertEquals("GOOGL", state.items.first().ticker)
            cancelAndIgnoreRemainingEvents()
        }

        verify { stocksSearchPager.createPagingSource("GOOGL") }
    }

    @Test
    fun whenSearchQueryCleared_thenCallsPagerWithEmptyQuery() = runTest {
        every { stocksSearchPager.createPagingSource("test") } returns
            FlowPagingSource(pageSize = 20) { _, _ ->
                PageResult(items = testStockOverviewLists, nextCursor = null)
            }

        underTest.stocksPagingState.test {
            advanceTimeBy(1100)
            expectMostRecentItem()

            underTest.onSearchQueryChange("test")
            advanceTimeBy(1100)
            expectMostRecentItem()

            underTest.onSearchQueryChange("")
            advanceTimeBy(1100)
            expectMostRecentItem()

            cancelAndIgnoreRemainingEvents()
        }

        verify { stocksSearchPager.createPagingSource("") }
        verify { stocksSearchPager.createPagingSource("test") }
    }

    @Test
    fun whenSameQueryRepeated_thenDebouncesProperly() = runTest {
        every { stocksSearchPager.createPagingSource("APP") } returns
            FlowPagingSource(pageSize = 20) { _, _ ->
                PageResult(items = testStockOverviewLists, nextCursor = null)
            }

        underTest.stocksPagingState.test {
            advanceTimeBy(1100)
            expectMostRecentItem()

            // Rapid-fire the same query — debounce + distinctUntilChanged should collapse these
            underTest.onSearchQueryChange("A")
            advanceTimeBy(200)
            underTest.onSearchQueryChange("AP")
            advanceTimeBy(200)
            underTest.onSearchQueryChange("APP")
            advanceTimeBy(1100)

            expectMostRecentItem()
            cancelAndIgnoreRemainingEvents()
        }

        // Only the final debounced value should reach the pager
        verify { stocksSearchPager.createPagingSource("APP") }
    }

    @Test
    fun whenEmptyResults_thenStillEmitsPagingData() = runTest {
        every { stocksSearchPager.createPagingSource("xyz") } returns
            FlowPagingSource(pageSize = 20) { _, _ ->
                PageResult(items = emptyList(), nextCursor = null)
            }

        underTest.stocksPagingState.test {
            advanceTimeBy(1100)
            expectMostRecentItem()

            underTest.onSearchQueryChange("xyz")
            advanceTimeBy(1100)

            val state = expectMostRecentItem()
            assertNotNull(state)
            assertTrue(state.items.isEmpty())
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private val testStockOverviewLists = listOf(
        Ticker(
            ticker = "AAPL",
            name = "Apple Inc.",
            market = "stocks",
            locale = "us",
            primaryExchange = "XNAS",
            type = "CS",
            active = true,
            currencyName = "usd",
            cik = null,
            compositeFigi = null,
            shareClassFigi = null,
            lastUpdatedUtc = null,
        ),
    )
}
