package com.core.network.model.stocksDetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockChartResponse(
    val ticker: String,
    val queryCount: Int,
    val resultsCount: Int,
    val adjusted: Boolean,
    val results: List<CandleResponse>,
    val status: String,
    @SerialName("request_id")
    val requestId: String,
    val count: Int,
)

@Serializable
data class CandleResponse(
    /** Volume */
    @SerialName("v")
    val volume: Double,
    /** Volume-weighted average price */
    @SerialName("vw")
    val vwap: Double? = null,
    @SerialName("o")
    val open: Double,
    @SerialName("c")
    val close: Double,
    @SerialName("h")
    val high: Double,
    @SerialName("l")
    val low: Double,
    /** Timestamp in epoch millis */
    @SerialName("t")
    val timestampMs: Long,
    /** Number of transactions */
    @SerialName("n")
    val transactions: Int,
)
