package com.juanfe.project.marketproductviewer.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanfe.project.marketproductviewer.R
import com.juanfe.project.marketproductviewer.domain.ExceptionService
import com.juanfe.project.marketproductviewer.domain.GetSearchHistoryUseCase
import com.juanfe.project.marketproductviewer.domain.SearchModel
import com.juanfe.project.marketproductviewer.domain.SearchProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchProductViewModel @Inject constructor(
    private val searchProductUseCase: SearchProductUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _searchHistory = MutableStateFlow<List<String>?>(null)
    val searchHistory: StateFlow<List<String>?> get() = _searchHistory

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
            SearchProductViewState.Error(context.getString(R.string.no_products))
        else _viewState.value = SearchProductViewState.Success(productList)
    }

}