package com.juanfe.project.marketproductviewer.domain

import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(private val searchProductRepository: SearchProductRepository) {
    operator fun invoke() = searchProductRepository.getSearchHistoryProducts()

}


