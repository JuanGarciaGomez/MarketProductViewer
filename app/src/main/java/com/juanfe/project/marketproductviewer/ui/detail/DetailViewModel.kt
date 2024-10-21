package com.juanfe.project.marketproductviewer.ui.detail

import androidx.lifecycle.ViewModel
import com.juanfe.project.marketproductviewer.domain.ResultModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow<DetailViewState>(DetailViewState.Success(null))
    val viewState: StateFlow<DetailViewState> = _viewState

    fun setProduct(product: ResultModel) {
        _viewState.value = DetailViewState.Success(product)
    }


}