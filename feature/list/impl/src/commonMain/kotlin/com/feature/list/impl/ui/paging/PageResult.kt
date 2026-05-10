package com.feature.list.impl.ui.paging

/**
 * Represents the result of loading a single page of data.
 *
 * @param T The type of items in the page.
 * @property items The list of items loaded in this page.
 * @property nextCursor The cursor to use for loading the next page, or null if there are no more pages.
 */
data class PageResult<T>(
    val items: List<T>,
    val nextCursor: String?,
)
