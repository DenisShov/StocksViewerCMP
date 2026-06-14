package com.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>,
): NavigationState {
    val topLevelStack = rememberSaveable { mutableStateListOf(startRoute) }
    val backStacks = rememberSaveable {
        topLevelRoutes.associateWith { key -> mutableStateListOf(key) }
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelStack = topLevelStack,
            backStacks = backStacks,
        )
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun NavigationState.toDecoratedEntries(
    entryProvider: org.koin.compose.navigation3.EntryProvider<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator<NavKey>(),
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider,
        )
    }

    return topLevelStack
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}

class NavigationState(
    val startRoute: NavKey,
    val topLevelStack: SnapshotStateList<NavKey>,
    val backStacks: Map<NavKey, SnapshotStateList<NavKey>>,
) {
    // The currently active top-level route
    val topLevelRoute: NavKey by derivedStateOf { topLevelStack.last() }

    // The current sub-stack for the active top-level route
    val currentSubStack: SnapshotStateList<NavKey>
        get() = backStacks[topLevelRoute]
            ?: error("Sub stack for $topLevelRoute does not exist")

    // The key at the top of the current sub-stack
    val currentKey: NavKey by derivedStateOf { currentSubStack.last() }
}
