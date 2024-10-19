package com.juanfe.project.marketproductviewer.domain

import javax.inject.Inject

class SearchProductUseCase @Inject constructor(private val searchProductRepository: SearchProductRepository) {
    suspend operator fun invoke(query: String) = searchProductRepository.searchProduct(query = query)

}


