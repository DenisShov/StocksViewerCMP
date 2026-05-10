package com.core.network.ktor

import arrow.core.Either
import com.core.network.model.errors.ApiError
import com.core.network.model.stocksDetails.StockChartResponse
import com.core.network.model.stocksDetails.StockOverviewResponse
import com.core.network.model.stocksList.TickersResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface StocksApiService {

    suspend fun searchStockByQuery(
        searchQuery: String,
        cursor: String? = null,
    ): Either<ApiError, TickersResponse>

    suspend fun getStockList(
        cursor: String? = null,
    ): Either<ApiError, TickersResponse>

    suspend fun getStockOverview(
        ticker: String,
    ): Either<ApiError, StockOverviewResponse>

    suspend fun getStockChartData(
        ticker: String,
        startDate: String,
        endDate: String,
        period: String,
    ): Either<ApiError, StockChartResponse>
}

class StocksApiServiceImpl(private val httpClient: HttpClient) : StocksApiService {

    override suspend fun searchStockByQuery(
        searchQuery: String,
        cursor: String?,
    ): Either<ApiError, TickersResponse> = safeApiCall {
        httpClient.get("v3/reference/tickers") {
            parameter("market", "stocks")
            parameter("active", "true")
            parameter("limit", "50")
            parameter("search", searchQuery)
            cursor?.let { parameter("cursor", it) }
        }.body()
    }

    override suspend fun getStockList(
        cursor: String?,
    ): Either<ApiError, TickersResponse> = safeApiCall {
        httpClient.get("v3/reference/tickers") {
            parameter("market", "stocks")
            parameter("active", "true")
            parameter("limit", "50")
            cursor?.let { parameter("cursor", it) }
        }.body()
    }

    override suspend fun getStockOverview(
        ticker: String,
    ): Either<ApiError, StockOverviewResponse> = safeApiCall {
        httpClient.get("v3/reference/tickers/$ticker").body()
    }

    override suspend fun getStockChartData(
        ticker: String,
        startDate: String,
        endDate: String,
        period: String,
    ): Either<ApiError, StockChartResponse> = safeApiCall {
        httpClient.get("v2/aggs/ticker/$ticker/range/1/$period/$startDate/$endDate") {
            parameter("adjusted", "true")
            parameter("sort", "asc")
            parameter("limit", "50000")
        }.body()
    }
}
