package com.juanfe.project.marketproductviewer.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanfe.project.marketproductviewer.domain.ExceptionService
import com.juanfe.project.marketproductviewer.domain.GetSearchHistoryUseCase
import com.juanfe.project.marketproductviewer.domain.SearchModel
import com.juanfe.project.marketproductviewer.domain.SearchProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchProductViewModel @Inject constructor(
    private val searchProductUseCase: SearchProductUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase
) : ViewModel() {


    private val _searchHistory = MutableStateFlow<String?>(null)
    val searchHistory: StateFlow<String?> get() = _searchHistory

    private val _viewState =
        MutableStateFlow<SearchProductViewState>(SearchProductViewState.Loading(firstOpen = true))
    val viewState: StateFlow<SearchProductViewState> = _viewState

    fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.SearchProduct -> search(intent.query)
            UserIntent.TapSearch -> getAllHistory()
        }
    }

    private fun getAllHistory() {
        viewModelScope.launch {
            getSearchHistoryUseCase.invoke().collect { history ->
                _searchHistory.value = history
            }
        }
    }

    private fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.value = SearchProductViewState.Loading()
            val result = searchProductUseCase.invoke(query)
            result.fold(onSuccess = { searchProduct ->
                handleSuccess(searchProduct)
            }, onFailure = {
                handleError(it)
            })
        }
    }

    private fun handleError(error: Throwable) {
        val exception = error as ExceptionService
        error.printStackTrace()
        _viewState.value = SearchProductViewState.Error(errorMsg = exception.msgError)
    }

    private fun handleSuccess(searchProduct: SearchModel) {
        val productList = searchProduct.results
        if (productList.isEmpty()) _viewState.value =
            SearchProductViewState.Error("No hay productos para esta busqueda")
        else _viewState.value = SearchProductViewState.Success(productList)
    }

}