package com.juanfe.project.marketproductviewer.data.repository

import com.juanfe.project.marketproductviewer.data.network.ProductService
import com.juanfe.project.marketproductviewer.domain.SearchModel
import com.juanfe.project.marketproductviewer.domain.SearchProductRepository
import toDomain
import javax.inject.Inject


class SearchProductRepositoryImpl @Inject constructor(private val productService: ProductService) :
    SearchProductRepository {
    override suspend fun searchProduct(query: String): Result<SearchModel> = runCatching {
        val response = productService.search(query)
        val body = response.body()
        body?.toDomain() ?: throw Exception("Null or empty")
    }

}


