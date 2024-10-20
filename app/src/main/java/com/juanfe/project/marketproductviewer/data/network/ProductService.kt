package com.juanfe.project.marketproductviewer.data.network

import com.juanfe.project.marketproductviewer.data.network.response.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    @GET("sites/MCO/search")
    suspend fun search(
        @Query("q") query: String,
    ): Response<SearchResponse>

}
