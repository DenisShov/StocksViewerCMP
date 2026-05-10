package com.feature.details.impl.ui.compose

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.core.designsystem.component.HandleError
import com.core.designsystem.icon.IconResources
import com.feature.details.impl.ui.StockDetailsViewModel
import com.feature.details.impl.ui.actions.ChartPeriod
import com.feature.details.impl.ui.actions.StockDetailsActions
import com.feature.details.impl.ui.compose.chart.CandleChart
import com.feature.details.impl.ui.model.CandleUiModel
import com.feature.details.impl.ui.model.StockOverviewUiModel
import com.feature.details.impl.ui.state.StockDetailsState
import org.jetbrains.compose.resources.stringResource
import stocksviewercmp.core.commonresources.generated.resources.Res
import stocksviewercmp.core.commonresources.generated.resources.a11y_logo
import stocksviewercmp.core.commonresources.generated.resources.a11y_return_to_previous_screen
import stocksviewercmp.core.commonresources.generated.resources.about
import stocksviewercmp.core.commonresources.generated.resources.add_to_favorites
import stocksviewercmp.core.commonresources.generated.resources.cik
import stocksviewercmp.core.commonresources.generated.resources.employees
import stocksviewercmp.core.commonresources.generated.resources.market_cap
import stocksviewercmp.core.commonresources.generated.resources.market_data
import stocksviewercmp.core.commonresources.generated.resources.read_more
import stocksviewercmp.core.commonresources.generated.resources.remove_from_favorites
import stocksviewercmp.core.commonresources.generated.resources.sector
import stocksviewercmp.core.commonresources.generated.resources.show_less

@Composable
fun StockDetailsRoute(
    viewModel: StockDetailsViewModel,
    onBackButtonClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val actions = StockDetailsActions(
        onChartPeriodChange = viewModel::getStockChartData,
        retry = viewModel::getStockOverviewByTicker,
        retryChart = viewModel::retryGetStockChartData,
        onToggleFavorite = viewModel::toggleFavorite,
    )

    StockDetailScreen(
        uiState = uiState,
        onBackButtonClick = onBackButtonClick,
        actions = actions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailScreen(
    uiState: StockDetailsState,
    onBackButtonClick: () -> Unit = {},
    actions: StockDetailsActions,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.testTag("stock_details_ticker_name"),
                        text = uiState.stockOverview?.ticker.orEmpty(),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackButtonClick.invoke() },
                        modifier = Modifier.testTag("stock_details_back_button"),
                    ) {
                        Icon(
                            imageVector = IconResources.ArrowBack,
                            contentDescription = stringResource(Res.string.a11y_return_to_previous_screen),
                            modifier = Modifier.padding(start = 12.dp),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = actions.onToggleFavorite,
                        modifier = Modifier.testTag("favorite_toggle_button"),
                    ) {
                        Icon(
                            imageVector = if (uiState.isFavorite) {
                                IconResources.StarFilled
                            } else {
                                IconResources.StarOutline
                            },
                            contentDescription = if (uiState.isFavorite) {
                                stringResource(Res.string.remove_from_favorites)
                            } else {
                                stringResource(Res.string.add_to_favorites)
                            },
                            tint = if (uiState.isFavorite) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                windowInsets = WindowInsets(),
            )
        },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    StockDetailsSkeleton()
                }

                uiState.stockOverview != null && uiState.candles.isNotEmpty() -> {
                    StockDetailsContent(
                        stockOverview = uiState.stockOverview,
                        candles = uiState.candles,
                        selectedPeriod = uiState.selectedPeriod,
                        isChartLoading = uiState.isChartLoading,
                        chartErrorString = uiState.chartErrorString,
                        actions = actions,
                    )
                }

                uiState.errorString != null -> {
                    HandleError(
                        errorMessage = uiState.errorString,
                        onRetry = actions.retry,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("stock_details_error"),
                    )
                }
            }
        }
    }
}

@Composable
private fun StockDetailsContent(
    stockOverview: StockOverviewUiModel,
    candles: List<CandleUiModel>,
    selectedPeriod: ChartPeriod,
    isChartLoading: Boolean,
    chartErrorString: String?,
    actions: StockDetailsActions,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("stock_details_content"),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { CompanyHeader(stock = stockOverview) }

        item { KeyStatsGrid(stock = stockOverview) }

        if (stockOverview.description.isNullOrEmpty().not()) {
            item {
                CompanyAbout(description = stockOverview.description)
            }
        }

        item {
            ContactInfo(stock = stockOverview)

            Chart(
                candles = candles,
                selectedPeriod = selectedPeriod,
                isChartLoading = isChartLoading,
                chartErrorString = chartErrorString,
                actions = actions,
            )
        }
    }
}

@Composable
private fun CompanyHeader(stock: StockOverviewUiModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.testTag("company_header"),
    ) {
        AsyncImage(
            model = stock.iconUrl,
            contentDescription = stringResource(Res.string.a11y_logo),
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .testTag("company_logo"),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = stock.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            SuggestionChip(
                onClick = { },
                label = { Text(stock.type) },
                modifier = Modifier.height(30.dp),
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                border = null
            )
        }
    }
}

@Composable
private fun KeyStatsGrid(stock: StockOverviewUiModel) {
    Column(modifier = Modifier.testTag("key_stats_grid")) {
        if (stock.marketCap != null && stock.totalEmployees != null || stock.sicDescription != null) {
            Text(
                text = stringResource(Res.string.market_data),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        if (stock.marketCap != null && stock.totalEmployees != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.market_cap),
                    value = stock.marketCap
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.employees),
                    value = formatNumber(stock.totalEmployees)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        if (stock.sicDescription != null) {
            StatCard(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.sector),
                value = stock.sicDescription,
            )
        }
    }
}

private fun formatNumber(value: Long): String {
    val str = value.toString()
    val result = StringBuilder()
    var count = 0
    for (i in str.length - 1 downTo 0) {
        result.append(str[i])
        count++
        if (count % 3 == 0 && i != 0) {
            result.append(',')
        }
    }
    return result.reverse().toString()
}

@Composable
private fun StatCard(modifier: Modifier = Modifier, label: String, value: String) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CompanyAbout(description: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.testTag("about_section")) {
        Text(
            text = stringResource(Res.string.about),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            onClick = { expanded = !expanded },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = if (expanded) Int.MAX_VALUE else 4,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )
                Text(
                    text = if (expanded) stringResource(Res.string.show_less) else stringResource(Res.string.read_more),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ContactInfo(stock: StockOverviewUiModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.testTag("contact_info"),
    ) {
        if (stock.address.isNullOrEmpty().not()) {
            ContactRow(
                icon = IconResources.LocationOn, text = stock.address
            )
        }
        if (stock.homepageUrl.isNullOrEmpty().not()) {
            ContactRow(
                icon = IconResources.Language, text = stock.homepageUrl
            )
        }
        if (stock.listDate.isNullOrEmpty().not()) {
            ContactRow(
                icon = IconResources.CalendarToday, text = stock.listDate
            )
        }
        if (stock.cik.isNullOrEmpty().not()) {
            ContactRow(
                icon = IconResources.Info, text = "${stringResource(Res.string.cik)} ${stock.cik}"
            )
        }
    }
}

@Composable
private fun ContactRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
internal fun StockDetailsSkeleton() {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f, animationSpec = infiniteRepeatable(
            animation = tween(1000), repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("stock_details_loading_skeleton"),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                    )
                }
            }
        }

        item {
            Column {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(74.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                    }

                }
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(74.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                repeat(4) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                    }
                }
            }
        }

        item {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Chart(
    candles: List<CandleUiModel>,
    selectedPeriod: ChartPeriod,
    isChartLoading: Boolean,
    chartErrorString: String?,
    actions: StockDetailsActions,
) {
    when {
        isChartLoading -> {
            ChartLoading()
        }

        chartErrorString != null -> {
            HandleError(
                errorMessage = chartErrorString,
                onRetry = actions.retryChart,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("stock_chart_details_error"),
            )
        }

        else -> {
            if (candles.isNotEmpty()) {
                Column(modifier = Modifier.testTag("chart_section")) {
                    CandleChart(
                        modifier = Modifier.testTag("stock_chart"),
                        data = candles,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PeriodButtons(
                        selectedPeriod = selectedPeriod,
                        onChartPeriodChange = actions.onChartPeriodChange
                    )
                }
            }
        }
    }
}

@Composable
private fun ChartLoading() {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f, animationSpec = infiniteRepeatable(
            animation = tween(1000), repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
    )
}
