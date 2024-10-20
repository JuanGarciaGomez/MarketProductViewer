package com.juanfe.project.marketproductviewer.data.network

import com.juanfe.project.marketproductviewer.data.network.response.InstallmentsResponse
import com.juanfe.project.marketproductviewer.data.network.response.PagingResponse
import com.juanfe.project.marketproductviewer.data.network.response.ResultResponse
import com.juanfe.project.marketproductviewer.data.network.response.SearchResponse
import com.juanfe.project.marketproductviewer.data.network.response.ShippingResponse
import com.juanfe.project.marketproductviewer.domain.InstallmentsModel
import com.juanfe.project.marketproductviewer.domain.PagingModel
import com.juanfe.project.marketproductviewer.domain.ResultModel
import com.juanfe.project.marketproductviewer.domain.SearchModel
import com.juanfe.project.marketproductviewer.domain.ShippingModel


fun SearchResponse.toDomain() = SearchModel(
    siteId = siteId,
    countryDefaultTimeZone = countryDefaultTimeZone,
    query = query,
    paging = paging.toDomain(),
    results = results.map { it.toDomain() }
)


fun PagingResponse.toDomain() =
    PagingModel(
        total = total,
        primaryResults = primaryResults,
        offset = offset,
        limit = limit
    )


fun ResultResponse.toDomain() =
    ResultModel(
        id = id,
        title = title,
        permalink = permalink,
        thumbnail = thumbnail,
        price = price,
        originalPrice = originalPrice,
        shipping = shipping.toDomain(),
        installments = installments?.toDomain(),

        )


fun InstallmentsResponse.toDomain() = InstallmentsModel(
    quantity = quantity,
    amount = amount,
    rate = rate,
    currencyId = currencyId
)


fun ShippingResponse.toDomain() = ShippingModel(
    freeShipping = freeShipping
)