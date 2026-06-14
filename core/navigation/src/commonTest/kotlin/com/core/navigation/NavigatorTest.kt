package com.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private data object StartKey : NavKey
private data object SecondTabKey : NavKey
private data class DetailKey(val id: String) : NavKey

class NavigatorTest {

    private lateinit var navigationState: NavigationState
    private lateinit var navigator: Navigator

    @BeforeTest
    fun setUp() {
        val topLevelStack = mutableStateListOf<NavKey>(StartKey)
        val backStacks = mapOf<NavKey, SnapshotStateList<NavKey>>(
            StartKey to mutableStateListOf<NavKey>(StartKey),
            SecondTabKey to mutableStateListOf<NavKey>(SecondTabKey),
        )
        navigationState = NavigationState(
            startRoute = StartKey,
            topLevelStack = topLevelStack,
            backStacks = backStacks,
        )
        navigator = Navigator()
        navigator.init(navigationState)
    }

    @Test
    fun navigate_adds_key_to_current_sub_stack() {
        val detail = DetailKey("123")

        navigator.navigate(detail)

        assertEquals(detail, navigationState.currentKey)
        assertEquals(2, navigationState.currentSubStack.size)
    }

    @Test
    fun navigate_switches_to_top_level_route() {
        navigator.navigate(SecondTabKey)

        assertEquals(SecondTabKey, navigationState.topLevelRoute)
    }

    @Test
    fun navigate_clears_sub_stack_when_navigating_to_current_tab() {
        val detail = DetailKey("123")
        navigator.navigate(detail)
        assertEquals(2, navigationState.currentSubStack.size)

        navigator.navigate(StartKey)

        assertEquals(1, navigationState.currentSubStack.size)
        assertEquals(StartKey, navigationState.currentKey)
    }

    @Test
    fun onBackClick_pops_current_sub_stack() {
        val detail = DetailKey("123")
        navigator.navigate(detail)

        navigator.onBackClick()

        assertEquals(StartKey, navigationState.currentKey)
        assertEquals(1, navigationState.currentSubStack.size)
    }

    @Test
    fun onBackClick_switches_to_previous_top_level_when_at_root_of_non_start_tab() {
        navigator.navigate(SecondTabKey)
        assertEquals(SecondTabKey, navigationState.topLevelRoute)

        navigator.onBackClick()

        assertEquals(StartKey, navigationState.topLevelRoute)
    }

    @Test
    fun canGoBack_returns_false_at_start_tab_root() {
        assertFalse(navigator.canGoBack())
    }

    @Test
    fun canGoBack_returns_true_when_sub_stack_has_entries() {
        navigator.navigate(DetailKey("123"))

        assertTrue(navigator.canGoBack())
    }

    @Test
    fun canGoBack_returns_true_on_non_start_tab() {
        navigator.navigate(SecondTabKey)

        assertTrue(navigator.canGoBack())
    }
}
