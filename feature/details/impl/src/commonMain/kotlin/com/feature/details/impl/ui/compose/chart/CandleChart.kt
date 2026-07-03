package com.feature.details.impl.ui.compose.chart

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.details.impl.ui.model.CandleUiModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Scroll
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.candlestickSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberCandlestickCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.ceil
import kotlin.math.floor

private const val Y_STEP = 10.0
private val StartAxisValueFormatter = CartesianValueFormatter.decimal()
private val StartAxisItemPlacer = VerticalAxis.ItemPlacer.step({ Y_STEP })

private val RangeProvider = object : CartesianLayerRangeProvider {
    override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
        Y_STEP * floor(minY / Y_STEP)

    override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
        Y_STEP * ceil(maxY / Y_STEP)
}

@Composable
private fun StockChartContent(
    modelProducer: CartesianChartModelProducer,
    time: List<Long>,
    modifier: Modifier = Modifier,
    axisLabelKey: ExtraStore.Key<Map<Int, String>> = ExtraStore.Key(),
) {
    val bottomAxisValueFormatter = remember(time) {
        CartesianValueFormatter { _, value, _ ->
            val timestamp = time.getOrNull(value.toInt())
            if (timestamp != null) {
                formatDate(timestamp)
            } else {
                "N/A"
            }
        }
    }

    val bottomAxisItemPlacer = remember(time) {
        HorizontalAxis.ItemPlacer.aligned(
            spacing = { 2 },
            addExtremeLabelPadding = true
        )
    }

    val chart = rememberCartesianChart(
        rememberCandlestickCartesianLayer(rangeProvider = RangeProvider),
        startAxis = VerticalAxis.rememberStart(
            valueFormatter = StartAxisValueFormatter,
            itemPlacer = StartAxisItemPlacer,
        ),
        bottomAxis = HorizontalAxis.rememberBottom(
            itemPlacer = bottomAxisItemPlacer,
            guideline = null,
            valueFormatter = bottomAxisValueFormatter,
        ),
        marker = rememberMarker(
            valueFormatter = DefaultCartesianMarker.ValueFormatter { context, targets ->
                val x = targets.firstOrNull()?.x?.toInt() ?: return@ValueFormatter ""
                val label = context.model.extraStore[axisLabelKey].get(x)
                label ?: ""
            }
        ),
    )
    val scrollState = rememberVicoScrollState(
        initialScroll = Scroll.Absolute.End
    )
    CartesianChartHost(
        chart = chart,
        modelProducer = modelProducer,
        scrollState = scrollState,
        modifier = modifier.height(400.dp),
    )
}

@Composable
fun CandleChart(
    modifier: Modifier = Modifier,
    data: ImmutableList<CandleUiModel>,
) {
    val time = data.map { it.timestampMs }
    val open = data.map { it.open }
    val close = data.map { it.close }
    val low = data.map { it.low }
    val high = data.map { it.high }

    val axisLabelKey = ExtraStore.Key<Map<Int, String>>()
    val axisLabels = remember(data) { getAxisLabels(data) }

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(data) {
        modelProducer.runTransaction {
            candlestickSeries(
                opening = open,
                closing = close,
                low = low,
                high = high
            )
            extras { it[axisLabelKey] = axisLabels }
        }
    }
    StockChartContent(
        modelProducer = modelProducer,
        time = time,
        modifier = modifier,
        axisLabelKey = axisLabelKey,
    )
}

private fun getAxisLabels(data: List<CandleUiModel>): MutableMap<Int, String> {
    val axisLabels = mutableMapOf<Int, String>()
    data.forEachIndexed { idx, candle ->
        val dateStr = formatDate(candle.timestampMs)
        axisLabels[idx] = "Date: $dateStr\n" +
            "Open: $${candle.open}\n" +
            "Close: $${candle.close}\n" +
            "Low: $${candle.low}\n" +
            "High: $${candle.high}"
    }
    return axisLabels
}

private val MONTH_NAMES = arrayOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)

private fun formatDate(millis: Long): String {
    val instant = kotlin.time.Instant.fromEpochMilliseconds(millis)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val month = MONTH_NAMES[localDateTime.month.ordinal]
    val day = localDateTime.day.toString().padStart(2, '0')
    val year = localDateTime.year
    return "$month $day $year"
}
