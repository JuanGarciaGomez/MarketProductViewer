package com.juanfe.project.marketproductviewer.ui.search

sealed class UserIntent() {
    data class SearchProduct(val query: String) : UserIntent()


}