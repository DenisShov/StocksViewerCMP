package com.feature.details.impl.domain.usecase

import arrow.core.Either
import com.core.common.error.DomainError
import com.feature.details.impl.domain.model.StockChart
import com.feature.details.impl.domain.repository.StocksDetailsRepository
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

open class GetStockChartDataUseCase(
    private val stocksDetailsRepository: StocksDetailsRepository,
) {
    open suspend fun launch(ticker: String, period: String): Either<DomainError, StockChart> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val currentDate = today.toString() // ISO-8601 format: yyyy-MM-dd
        val dateMinusTwoYears = today.minus(2, DateTimeUnit.YEAR).toString()

        return stocksDetailsRepository.getStockChartData(
            ticker = ticker,
            startDate = dateMinusTwoYears,
            endDate = currentDate,
            period = period,
        )
    }
}
