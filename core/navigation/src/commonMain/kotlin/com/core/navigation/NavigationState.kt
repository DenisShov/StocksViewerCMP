package com.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList

@Composable
fun rememberNavigationState(
    startRoute: Any,
    topLevelRoutes: Set<Any>,
): NavigationState {
    val topLevelStack = remember { mutableStateListOf(startRoute) }
    val backStacks = remember {
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

class NavigationState(
    val startRoute: Any,
    val topLevelStack: SnapshotStateList<Any>,
    val backStacks: Map<Any, SnapshotStateList<Any>>,
) {
    // The currently active top-level route
    val topLevelRoute: Any by derivedStateOf { topLevelStack.last() }

    // The current sub-stack for the active top-level route
    val currentSubStack: SnapshotStateList<Any>
        get() = backStacks[topLevelRoute]
            ?: error("Sub stack for $topLevelRoute does not exist")

    // The key at the top of the current sub-stack
    val currentKey: Any by derivedStateOf { currentSubStack.last() }

    // The full back stack combining top-level and sub-stacks for NavDisplay
    val currentBackStack: SnapshotStateList<Any>
        get() = currentSubStack
}
