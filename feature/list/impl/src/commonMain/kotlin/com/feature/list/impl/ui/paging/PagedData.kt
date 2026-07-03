package com.feature.list.impl.ui.paging

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PagedData<T>(
    val items: ImmutableList<T> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val hasMore: Boolean = true,
)
