package com.feature.details.impl.ui.compose

import androidx.compose.runtime.Composable
import com.core.designsystem.component.BackgroundPreview
import com.core.designsystem.theme.AppTheme
import com.feature.details.impl.ui.actions.ChartPeriod

@BackgroundPreview
@Composable
fun PeriodButtonsPreview() {
    AppTheme {
        PeriodButtons(selectedPeriod = ChartPeriod.WEEK, onChartPeriodChange = {})
    }
}
