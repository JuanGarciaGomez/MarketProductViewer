package com.juanfe.project.marketproductviewer.ui.detail

import com.juanfe.project.marketproductviewer.domain.ResultModel

sealed class DetailViewState() {
    data class Success(val resultModel: ResultModel?) : DetailViewState()

}