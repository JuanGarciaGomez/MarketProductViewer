package com.juanfe.project.marketproductviewer.ui.search

import com.juanfe.project.marketproductviewer.domain.ResultModel

sealed class SearchProductViewState() {
    data object Loading : SearchProductViewState()
    data object Error : SearchProductViewState()
    data class Success(val resultModel: List<ResultModel>) :
        SearchProductViewState()

}