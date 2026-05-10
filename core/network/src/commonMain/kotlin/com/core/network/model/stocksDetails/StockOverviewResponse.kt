package com.core.network.model.stocksDetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockOverviewResponse(
    @SerialName("request_id")
    val requestId: String,
    val results: CompanyResponse,
    val status: String,
)

@Serializable
data class CompanyResponse(
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
    @SerialName("market_cap")
    val marketCap: Double? = null,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    val address: AddressResponse? = null,
    val description: String? = null,
    @SerialName("sic_code")
    val sicCode: String? = null,
    @SerialName("sic_description")
    val sicDescription: String? = null,
    @SerialName("ticker_root")
    val tickerRoot: String? = null,
    @SerialName("homepage_url")
    val homepageUrl: String? = null,
    @SerialName("total_employees")
    val totalEmployees: Long? = null,
    @SerialName("list_date")
    val listDate: String? = null,
    val branding: BrandingResponse? = null,
    @SerialName("share_class_shares_outstanding")
    val shareClassSharesOutstanding: Long? = null,
    @SerialName("weighted_shares_outstanding")
    val weightedSharesOutstanding: Long? = null,
    @SerialName("round_lot")
    val roundLot: Long? = null,
)

@Serializable
data class AddressResponse(
    @SerialName("address1")
    val address1: String? = null,
    val city: String? = null,
    val state: String? = null,
    @SerialName("postal_code")
    val postalCode: String? = null,
)

@Serializable
data class BrandingResponse(
    @SerialName("logo_url")
    val logoUrl: String? = null,
    @SerialName("icon_url")
    val iconUrl: String? = null,
)
