package com.feature.list.impl.ui.paging

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * A multiplatform-compatible Flow-based paging source that replaces AndroidX Paging 3.
 *
 * Loads data in pages using a cursor-based approach and exposes the current state
 * as a [StateFlow] of [PagedData].
 *
 * @param T The type of items being paged.
 * @param pageSize The number of items to request per page.
 * @param loadPage A suspend function that loads a page given a cursor (null for the first page)
 *                 and returns a [PageResult] with items and the next cursor.
 */
class FlowPagingSource<T>(
    private val pageSize: Int = 50,
    private val loadPage: suspend (cursor: String?, pageSize: Int) -> PageResult<T>,
) {
    private val _state = MutableStateFlow(PagedData<T>())
    val state: StateFlow<PagedData<T>> = _state.asStateFlow()

    private var currentCursor: String? = null
    private val mutex = Mutex()

    /**
     * Loads the next page of data using the cursor from the previous page response.
     * If already loading or there are no more pages, this is a no-op.
     */
    @Suppress("TooGenericExceptionCaught")
    suspend fun loadNextPage() {
        mutex.withLock {
            val currentState = _state.value
            if (currentState.isLoading || !currentState.hasMore) return

            _state.update { it.copy(isLoading = true, error = null) }
        }

        try {
            val result = loadPage(currentCursor, pageSize)

            mutex.withLock {
                currentCursor = result.nextCursor
                _state.update { current ->
                    current.copy(
                        items = current.items + result.items,
                        isLoading = false,
                        error = null,
                        hasMore = result.nextCursor != null,
                    )
                }
            }
        } catch (e: Throwable) {
            mutex.withLock {
                _state.update { it.copy(isLoading = false, error = e) }
            }
        }
    }

    /**
     * Clears all loaded data and reloads from the beginning (first page).
     */
    @Suppress("TooGenericExceptionCaught")
    suspend fun refresh() {
        mutex.withLock {
            currentCursor = null
            _state.value = PagedData(isLoading = true)
        }

        try {
            val result = loadPage(null, pageSize)

            mutex.withLock {
                currentCursor = result.nextCursor
                _state.value = PagedData(
                    items = result.items,
                    isLoading = false,
                    error = null,
                    hasMore = result.nextCursor != null,
                )
            }
        } catch (e: Throwable) {
            mutex.withLock {
                _state.update { it.copy(isLoading = false, error = e) }
            }
        }
    }
}
