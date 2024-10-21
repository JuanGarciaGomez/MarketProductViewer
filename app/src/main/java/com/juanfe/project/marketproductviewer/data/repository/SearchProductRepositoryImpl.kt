package com.juanfe.project.marketproductviewer.data.repository

import android.content.Context
import com.juanfe.project.marketproductviewer.R
import com.juanfe.project.marketproductviewer.data.local.DataStoreManager
import com.juanfe.project.marketproductviewer.data.network.ProductService
import com.juanfe.project.marketproductviewer.data.network.toDomain
import com.juanfe.project.marketproductviewer.domain.ExceptionService
import com.juanfe.project.marketproductviewer.domain.SearchModel
import com.juanfe.project.marketproductviewer.domain.SearchProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.net.UnknownHostException
import javax.inject.Inject


class SearchProductRepositoryImpl @Inject constructor(
    private val productService: ProductService,
    private val dataStoreManager: DataStoreManager,
    @ApplicationContext private val context: Context
) : SearchProductRepository {

    private val error = context.getString(R.string.unknown_error)
    override suspend fun searchProduct(query: String): Result<SearchModel> {
        saveQueryHistory(query)
        val response = runCatching {
            productService.search(query)
        }
        val searchModel = response.fold(
            onSuccess = {
                if (it.code() == 200) {
                    val body = it.body()
                    body?.toDomain() ?: return Result.failure(ExceptionService(error))
                } else {
                    return Result.failure(handleThrow(it.code()))
                }
            },
            onFailure = {
                // With this method validated the retrofit exception
                when (it) {
                    is UnknownHostException -> {
                        return Result.failure(ExceptionService(context.getString(R.string.internet_error)))
                    }

                    else -> {
                        return Result.failure(ExceptionService(error))
                    }
                }
            }
        )
        return Result.success(searchModel)
    }

    private suspend fun saveQueryHistory(query: String) {
        val currentHistory = dataStoreManager.allHistory.first()
        val updatedHistory = removeDuplicatesAndJoin(query, currentHistory)
        dataStoreManager.saveSearch(updatedHistory)
    }

    override fun getSearchHistoryProducts(): Flow<List<String>> {
        return dataStoreManager.allHistory.map { history ->
            if (history.isEmpty()) listOf()
            else history.split(",")
        }
    }

    /**
     * In this handle Throw we control the exceptions from Service
     */
    private fun handleThrow(code: Int): Exception {
        return when (code) {
            in 400..499 -> ExceptionService(context.getString(R.string.client_error))
            in 500..599 -> ExceptionService(context.getString(R.string.server_error))
            else -> Exception(error)
        }
    }

    private fun removeDuplicatesAndJoin(query: String, currentHistory: String): String {
        val finalHistory = if (currentHistory.isEmpty()) mutableSetOf()
        else currentHistory.split(",").toMutableSet()
        if (query.isNotBlank()) {
            finalHistory.add(query.trim())
        }
        return finalHistory.joinToString(",").trim()
    }
}


