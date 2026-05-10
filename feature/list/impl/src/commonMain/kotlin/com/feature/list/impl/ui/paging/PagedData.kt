package com.feature.list.impl.ui.paging

/**
 * Represents the current state of paginated data.
 *
 * @param T The type of items in the paged list.
 * @property items All items loaded so far across all pages.
 * @property isLoading Whether a page load is currently in progress.
 * @property error The error from the last failed page load, or null if no error.
 * @property hasMore Whether there are more pages available to load.
 */
data class PagedData<T>(
    val items: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val hasMore: Boolean = true,
)
