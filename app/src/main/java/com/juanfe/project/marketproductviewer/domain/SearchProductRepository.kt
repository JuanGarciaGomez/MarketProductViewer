package com.juanfe.project.marketproductviewer.domain

interface SearchProductRepository {
    suspend fun searchProduct(query: String): Result<SearchModel>

}