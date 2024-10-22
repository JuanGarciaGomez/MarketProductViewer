package com.juanfe.project.marketproductviewer.data.network.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("site_id")
    val siteId: String,
    @SerializedName("country_default_time_zone")
    val countryDefaultTimeZone: String,
    val query: String,
    val paging: PagingResponse,
    val results: List<ResultResponse>,
)

data class PagingResponse(
    val total: Long,
    @SerializedName("primary_results")
    val primaryResults: Long,
    val offset: Long,
    val limit: Long,
)

data class ResultResponse(
    val id: String,
    val title: String,
    val permalink: String,
    val thumbnail: String,
    val price: Double,
    @SerializedName("original_price")
    val originalPrice: Double,
    val shipping: ShippingResponse,
    val installments: InstallmentsResponse? = null,
)

data class InstallmentsResponse(
    val quantity: Int,
    val amount: Double,
    val rate: Int,
    @SerializedName("currency_id")
    val currencyId: String,
)

data class ShippingResponse(
    @SerializedName("free_shipping")
    val freeShipping: Boolean,
)
