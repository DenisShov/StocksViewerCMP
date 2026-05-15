package com.core.designsystem.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import com.core.designsystem.icon.IconResources
import org.jetbrains.compose.resources.stringResource
import stocksviewercmp.core.resources.generated.resources.Res
import stocksviewercmp.core.resources.generated.resources.a11y_close_search
import stocksviewercmp.core.resources.generated.resources.a11y_search_description
import stocksviewercmp.core.resources.generated.resources.all_stocks
import stocksviewercmp.core.resources.generated.resources.search_stocks

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClose: () -> Unit,
    onSearchOpen: () -> Unit,
    isSearching: Boolean,
) {
    CenterAlignedTopAppBar(
        title = {
            if (isSearching) {
                SearchTextField(
                    query = query,
                    onQueryChange = onQueryChange,
                )
            } else {
                Text(
                    text = stringResource(Res.string.all_stocks),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        navigationIcon = {
            if (isSearching) {
                IconButton(onClick = onSearchClose) {
                    Icon(
                        imageVector = IconResources.ArrowBack,
                        contentDescription = stringResource(Res.string.a11y_close_search)
                    )
                }
            }
        },
        actions = {
            if (!isSearching) {
                IconButton(onClick = onSearchOpen) {
                    Icon(
                        imageVector = IconResources.Search,
                        contentDescription = stringResource(Res.string.a11y_search_description),
                    )
                }
            }
        },
        windowInsets = WindowInsets(),
    )
}

@Composable
fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                stringResource(Res.string.search_stocks),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )
}
