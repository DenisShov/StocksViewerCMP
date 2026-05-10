package com.feature.list.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.list.impl.ui.model.TickerUiModel
import com.feature.list.impl.ui.model.toUiModel
import com.feature.list.impl.ui.paging.FlowPagingSource
import com.feature.list.impl.ui.paging.PagedData
import com.feature.list.impl.ui.paging.StocksSearchPager
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class StocksListViewModel(
    private val stocksSearchPager: StocksSearchPager,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    private var currentPagingSource: FlowPagingSource<com.feature.list.impl.domain.model.Ticker>? = null
    private var initialLoadJob: Job? = null

    val stocksPagingState: StateFlow<PagedData<TickerUiModel>> = searchQuery
        .debounce(1000)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            val pagingSource = stocksSearchPager.createPagingSource(query)
            currentPagingSource = pagingSource

            // Trigger initial page load
            initialLoadJob?.cancel()
            initialLoadJob = viewModelScope.launch {
                pagingSource.loadNextPage()
            }

            pagingSource.state.map { pagedData ->
                PagedData(
                    items = pagedData.items.map { it.toUiModel() },
                    isLoading = pagedData.isLoading,
                    error = pagedData.error,
                    hasMore = pagedData.hasMore,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagedData(isLoading = true),
        )

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun loadNextPage() {
        viewModelScope.launch {
            currentPagingSource?.loadNextPage()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            currentPagingSource?.refresh()
        }
    }
}
