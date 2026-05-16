package com.test.app.stocksviewercmp.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.core.designsystem.theme.AppTheme
import com.core.navigation.Navigator
import com.core.navigation.rememberNavigationState
import com.feature.details.api.StocksDetailKey
import com.feature.details.impl.ui.StockDetailsViewModel
import com.feature.details.impl.ui.compose.StockDetailsRoute
import com.feature.favorites.api.FavoritesListKey
import com.feature.favorites.impl.ui.compose.FavoritesListRoute
import com.feature.list.api.StocksListKey
import com.feature.list.impl.ui.compose.StocksListRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun StocksApp() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val navigationState = rememberNavigationState(
                startRoute = StocksListKey,
                topLevelRoutes = setOf(StocksListKey, FavoritesListKey),
            )

            val navigator = remember { Navigator(navigationState) }

            val showBottomBar = navigationState.currentKey is FavoritesListKey ||
                    navigationState.currentKey is StocksListKey

            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        NavigationBar {
                            TopLevelTab.entries.forEach { tab ->
                                val selected = navigationState.topLevelRoute == tab.key
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { navigator.navigate(tab.key) },
                                    icon = {
                                        Icon(
                                            if (selected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.label,
                                        )
                                    },
                                    label = { Text(tab.label) },
                                )
                            }
                        }
                    }
                },
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    NavDisplay(
                        backStack = navigationState.currentBackStack,
                        onBack = { navigator.onBackClick() },
                        entryProvider = { key ->
                            when (key) {
                                is StocksListKey -> NavEntry(key) {
                                    StocksListRoute(
                                        onStockClick = { ticker ->
                                            navigator.navigate(StocksDetailKey(ticker))
                                        },
                                    )
                                }

                                is StocksDetailKey -> NavEntry(key) {
                                    val viewModel = koinViewModel<StockDetailsViewModel>(
                                        key = key.stockTicker,
                                    ) { parametersOf(key.stockTicker) }
                                    StockDetailsRoute(
                                        viewModel = viewModel,
                                        onBackButtonClick = navigator::onBackClick,
                                    )
                                }

                                is FavoritesListKey -> NavEntry(key) {
                                    FavoritesListRoute(
                                        onStockClick = { ticker ->
                                            navigator.navigate(StocksDetailKey(ticker))
                                        },
                                    )
                                }

                                else -> error("Not correct navigation key")
                            }
                        },
                    )
                }
            }
        }
    }
}
