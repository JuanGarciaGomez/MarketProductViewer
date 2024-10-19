package com.juanfe.project.marketproductviewer.domain

data class SearchModel(
    val siteId: String,
    val countryDefaultTimeZone: String,
    val query: String,
    val paging: PagingModel,
    val results: List<ResultModel>,
)

data class PagingModel(
    val total: Long,
    val primaryResults: Long,
    val offset: Long,
    val limit: Long,
)

data class ResultModel(
    val id: String,
    val title: String,
    val permalink: String,
    val thumbnail: String,
    val price: Double,
    val originalPrice: Double,
    val shipping: ShippingModel,
    val installments: InstallmentsModel? = null,
)

data class InstallmentsModel(
    val quantity: Int,
    val amount: Double,
    val rate: Int,
    val currencyId: String,
)

data class ShippingModel(
    val freeShipping: Boolean,
)