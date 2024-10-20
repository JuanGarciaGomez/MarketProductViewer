package com.juanfe.project.marketproductviewer.ui.search

import com.juanfe.project.marketproductviewer.domain.ResultModel

sealed class SearchProductViewState() {
    data class Loading(val firstOpen: Boolean = false) : SearchProductViewState()
    data class Error(val errorMsg: String) : SearchProductViewState()
    data class Success(val resultModel: List<ResultModel>) :
        SearchProductViewState()

}