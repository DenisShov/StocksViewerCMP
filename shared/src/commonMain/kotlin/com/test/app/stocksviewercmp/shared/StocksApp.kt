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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import com.core.designsystem.theme.AppTheme
import com.core.navigation.Navigator
import com.core.navigation.rememberNavigationState
import com.core.navigation.toDecoratedEntries
import com.feature.favorites.api.FavoritesListKey
import com.feature.list.api.StocksListKey
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
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

            val navigator: Navigator = koinInject()
            LaunchedEffect(navigationState) {
                navigator.init(navigationState)
            }

            val showBottomBar = shouldShowBottomBar(navigationState.currentKey)

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
                        entries = navigationState.toDecoratedEntries(koinEntryProvider()),
                        onBack = { navigator.onBackClick() },
                    )
                }
            }
        }
    }
}

internal fun shouldShowBottomBar(currentKey: Any): Boolean =
    currentKey is StocksListKey || currentKey is FavoritesListKey
