package com.core.network.model.stocksList

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TickersResponse(
    val results: List<TickerResponse>,
    val status: String,
    @SerialName("request_id")
    val requestId: String,
    val count: Int,
    @SerialName("next_url")
    val nextUrl: String? = null,
)

@Serializable
data class TickerResponse(
    val ticker: String,
    val name: String,
    val market: String,
    val locale: String,
    @SerialName("primary_exchange")
    val primaryExchange: String,
    val type: String,
    val active: Boolean,
    @SerialName("currency_name")
    val currencyName: String? = null,
    val cik: String? = null,
    @SerialName("composite_figi")
    val compositeFigi: String? = null,
    @SerialName("share_class_figi")
    val shareClassFigi: String? = null,
    @SerialName("last_updated_utc")
    val lastUpdatedUtc: String? = null,
)
