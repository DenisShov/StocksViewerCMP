package com.feature.list.impl.ui.paging

data class PageResult<T>(
    val items: List<T>,
    val nextCursor: String?,
)
