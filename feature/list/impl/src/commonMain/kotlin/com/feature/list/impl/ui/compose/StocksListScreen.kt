package com.feature.list.impl.ui.compose

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.core.designsystem.component.HandleError
import com.core.designsystem.component.SearchTopAppBar
import com.core.ui.ErrorRetryItem
import com.feature.list.impl.ui.StocksListViewModel
import com.feature.list.impl.ui.model.TickerUiModel
import com.feature.list.impl.ui.paging.PagedData
import com.feature.list.impl.ui.paging.SearchResultsError
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import stocksviewercmp.core.resources.generated.resources.Res
import stocksviewercmp.core.resources.generated.resources.no_network_connection
import stocksviewercmp.core.resources.generated.resources.some_server_problem
import stocksviewercmp.core.resources.generated.resources.something_went_wrong

@Composable
fun StocksListRoute(
    viewModel: StocksListViewModel = koinViewModel(),
    onStockClick: (String) -> Unit,
) {
    val pagedData by viewModel.stocksPagingState.collectAsState()

    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    StocksListScreen(
        pagedData = pagedData,
        pullToRefreshState = pullToRefreshState,
        isRefreshing = isRefreshing,
        onStockClick = onStockClick,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onLoadMore = viewModel::loadNextPage,
        onRefresh = {
            isRefreshing = true
            viewModel.refresh()
        },
        onRefreshComplete = { isRefreshing = false },
    )
}

@Composable
fun StocksListScreen(
    pagedData: PagedData<TickerUiModel>,
    pullToRefreshState: PullToRefreshState,
    isRefreshing: Boolean,
    onStockClick: (String) -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onLoadMore: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onRefreshComplete: () -> Unit = {},
) {
    var isSearching by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }

    // Complete refresh when loading finishes
    LaunchedEffect(pagedData.isLoading, isRefreshing) {
        if (!pagedData.isLoading && isRefreshing) {
            onRefreshComplete()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
            ),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            SearchTopAppBar(
                query = query,
                onQueryChange = {
                    query = it
                    onSearchQueryChange(it)
                },
                onSearchClose = {
                    query = ""
                    isSearching = false
                    onSearchQueryChange("")
                },
                onSearchOpen = {
                    isSearching = true
                },
                isSearching = isSearching,
            )
        },
        content = { padding ->
            StocksListContent(
                pagedData = pagedData,
                onStockClick = onStockClick,
                onLoadMore = onLoadMore,
                onRetry = onLoadMore,
                isRefreshing = isRefreshing,
                pullToRefreshState = pullToRefreshState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        }
    )
}

@Composable
private fun StocksListContent(
    pagedData: PagedData<TickerUiModel>,
    onStockClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    isRefreshing: Boolean,
    pullToRefreshState: PullToRefreshState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when {
            pagedData.isLoading && pagedData.items.isEmpty() -> {
                StocksListSkeleton()
            }

            pagedData.error != null && pagedData.items.isEmpty() -> {
                HandleError(
                    errorMessage = getErrorMessage(pagedData.error),
                    onRetry = onRetry,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("stocks_list_error"),
                )
            }

            else -> {
                val listState = rememberLazyListState()

                // Trigger loading more when near the end of the list
                val shouldLoadMore by remember {
                    derivedStateOf {
                        val lastVisibleItem =
                            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                        val totalItems = listState.layoutInfo.totalItemsCount
                        lastVisibleItem >= totalItems - 5 && !pagedData.isLoading && pagedData.hasMore
                    }
                }

                LaunchedEffect(shouldLoadMore) {
                    if (shouldLoadMore) {
                        onLoadMore()
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("stocks_list"),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(top = 16.dp),
                ) {
                    items(count = pagedData.items.size) { index ->
                        StockListItem(
                            stockItem = pagedData.items[index],
                            onStockClick = onStockClick
                        )
                    }

                    // Loading indicator for next page
                    if (pagedData.isLoading && pagedData.items.isNotEmpty()) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(vertical = 6.dp)
                                    .testTag("stocks_list_append_loading")
                            )
                        }
                    }

                    // Error indicator for next page load failure
                    if (pagedData.error != null && pagedData.items.isNotEmpty()) {
                        item {
                            Box {
                                ErrorRetryItem(
                                    error = pagedData.error.message,
                                    onTryClicked = onRetry,
                                )
                            }
                        }
                    }
                }

                if (isRefreshing) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                    )
                } else {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth(),
                        progress = { pullToRefreshState.distanceFraction },
                    )
                }
            }
        }
    }
}

@Composable
private fun getErrorMessage(error: Throwable?): String {
    return when (error) {
        is SearchResultsError.HttpError -> error.errorMessage
            ?: stringResource(Res.string.some_server_problem)

        is SearchResultsError.NetworkError -> stringResource(Res.string.no_network_connection)
        is SearchResultsError.UnknownError -> stringResource(Res.string.something_went_wrong)
        else -> error?.message ?: stringResource(Res.string.something_went_wrong)
    }
}

@Composable
internal fun StocksListSkeleton() {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("stocks_list_loading_skeleton"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp),
    ) {
        items(10) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(20.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                    }
                }
            }
        }
    }
}
