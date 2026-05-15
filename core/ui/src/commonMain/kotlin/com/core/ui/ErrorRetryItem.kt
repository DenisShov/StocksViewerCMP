package com.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import stocksviewercmp.core.resources.generated.resources.Res
import stocksviewercmp.core.resources.generated.resources.retry
import stocksviewercmp.core.resources.generated.resources.some_error_happened

@Composable
fun ErrorRetryItem(error: String?, onTryClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .testTag("stocks_list_append_error"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .padding(vertical = 8.dp),
            text = error ?: stringResource(Res.string.some_error_happened),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium
        )
        Button(
            onClick = onTryClicked
        ) {
            Text(
                text = stringResource(Res.string.retry),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
