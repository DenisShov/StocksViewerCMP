package com.feature.list.impl.ui.paging

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FlowPagingSource<T>(
    private val pageSize: Int = 50,
    private val loadPage: suspend (cursor: String?, pageSize: Int) -> PageResult<T>,
) {
    private val _state = MutableStateFlow(PagedData<T>())
    val state: StateFlow<PagedData<T>> = _state.asStateFlow()

    private var currentCursor: String? = null

    suspend fun loadNextPage() {
        val currentState = _state.value
        if (currentState.isLoading || !currentState.hasMore) return

        _state.update { it.copy(isLoading = true, error = null) }

        try {
            val result = loadPage(currentCursor, pageSize)

            currentCursor = result.nextCursor
            _state.update { current ->
                current.copy(
                    items = (current.items + result.items).toImmutableList(),
                    isLoading = false,
                    error = null,
                    hasMore = result.nextCursor != null,
                )
            }
        } catch (e: SearchResultsError) {
            _state.update { it.copy(isLoading = false, error = e) }
        }
    }

    suspend fun refresh() {
        currentCursor = null
        _state.value = PagedData(isLoading = true)

        try {
            val result = loadPage(null, pageSize)

            currentCursor = result.nextCursor
            _state.value = PagedData(
                items = result.items.toImmutableList(),
                isLoading = false,
                error = null,
                hasMore = result.nextCursor != null,
            )
        } catch (e: SearchResultsError) {
            _state.update { it.copy(isLoading = false, error = e) }
        }
    }
}
