package com.feature.details.impl.ui.compose.chart

import androidx.compose.runtime.Composable
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.DefaultCartesianMarker.ValueFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent

@Composable
fun rememberMarker(valueFormatter: ValueFormatter): CartesianMarker {
    val label = rememberTextComponent()
    return rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = valueFormatter,
    )
}
