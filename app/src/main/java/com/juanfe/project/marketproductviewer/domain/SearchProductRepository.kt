package com.juanfe.project.marketproductviewer.domain

import kotlinx.coroutines.flow.Flow

interface SearchProductRepository {
    suspend fun searchProduct(query: String): Result<SearchModel>
    fun getSearchHistoryProducts(): Flow<String>

}