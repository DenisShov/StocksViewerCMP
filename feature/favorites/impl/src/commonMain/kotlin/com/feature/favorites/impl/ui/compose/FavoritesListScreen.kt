package com.feature.favorites.impl.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feature.favorites.impl.ui.FavoritesListViewModel
import com.feature.favorites.impl.ui.state.FavoritesListState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import stocksviewercmp.core.commonresources.generated.resources.Res
import stocksviewercmp.core.commonresources.generated.resources.favorites_empty
import stocksviewercmp.core.commonresources.generated.resources.favorites_loading
import stocksviewercmp.core.commonresources.generated.resources.favorites_title

@Composable
fun FavoritesListRoute(
    viewModel: FavoritesListViewModel = koinViewModel(),
    onStockClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    FavoritesListScreen(uiState = uiState, onStockClick = onStockClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesListScreen(
    uiState: FavoritesListState,
    onStockClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.favorites_title),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("favorites_title"),
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                windowInsets = WindowInsets(0),
            )
        },
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when (uiState) {
                is FavoritesListState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("favorites_loading"),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(Res.string.favorites_loading),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                is FavoritesListState.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("favorites_empty"),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(Res.string.favorites_empty),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 32.dp),
                        )
                    }
                }

                is FavoritesListState.Content -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("favorites_list"),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                    ) {
                        items(
                            items = uiState.favorites,
                            key = { it.ticker },
                        ) { stock ->
                            FavoriteStockItem(
                                stock = stock,
                                onClick = { onStockClick(stock.ticker) },
                            )
                        }
                    }
                }
            }
        }
    }
}
