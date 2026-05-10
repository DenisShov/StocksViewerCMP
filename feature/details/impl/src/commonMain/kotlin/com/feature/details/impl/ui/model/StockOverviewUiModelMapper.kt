package com.feature.details.impl.ui.model

import com.feature.details.impl.domain.model.Address
import com.feature.details.impl.domain.model.StockOverview
import kotlinx.datetime.LocalDate

fun StockOverview.toUiModel() =
    StockOverviewUiModel(
        ticker = results.ticker,
        name = results.name,
        locale = results.locale,
        type = getType(results.type),
        address = getAddress(results.address),
        exchange = results.primaryExchange,
        currencyName = results.currencyName,
        marketCap = results.marketCap?.let { formatMarketCap(it) },
        description = results.description,
        homepageUrl = results.homepageUrl,
        totalEmployees = results.totalEmployees,
        sicDescription = results.sicDescription,
        cik = results.cik,
        listDate = formatDate(results.listDate),
        iconUrl = results.branding?.iconUrl,
    )

private fun getType(type: String): String = when (type) {
    "CS" -> "Common Stock"
    "ETF" -> "Exchange Traded Fund"
    "ADRC" -> "Depositary Receipt"
    else -> type
}

private fun getAddress(address: Address?) = address?.run {
    listOfNotNull(
        address1,
        city,
        state,
        postalCode,
    ).joinToString(", ")
} ?: ""

private fun formatDate(dateString: String?): String? {
    return dateString?.let {
        try {
            val date = LocalDate.parse(it) // ISO-8601 format: yyyy-MM-dd
            val day = date.day
            val month = date.month.name.lowercase().replaceFirstChar { c -> c.uppercase() }
            val year = date.year
            "$day $month $year"
        } catch (_: Exception) {
            dateString
        }
    }
}

private fun formatMarketCap(marketCap: Double): String {
    return when {
        marketCap >= 1_000_000_000_000 -> "${formatDouble(marketCap / 1_000_000_000_000)}T"
        marketCap >= 1_000_000_000 -> "${formatDouble(marketCap / 1_000_000_000)}B"
        marketCap >= 1_000_000 -> "${formatDouble(marketCap / 1_000_000)}M"
        marketCap >= 1_000 -> "${formatDouble(marketCap / 1_000)}K"
        else -> formatDouble(marketCap)
    }
}

private fun formatDouble(value: Double): String {
    val rounded = (value * 100).toLong() / 100.0
    val intPart = rounded.toLong()
    val fracPart = ((rounded - intPart) * 100).toLong()
    return "$intPart.${fracPart.toString().padStart(2, '0')}"
}
