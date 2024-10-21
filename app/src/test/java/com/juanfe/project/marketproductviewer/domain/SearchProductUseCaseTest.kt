package com.juanfe.project.marketproductviewer.domain

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SearchProductUseCaseTest {

    @Mock
    private lateinit var searchProductRepository: SearchProductRepository


    private lateinit var searchProductUseCase: SearchProductUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchProductUseCase = SearchProductUseCase(searchProductRepository)
    }

    @Test
    fun `invoke should return searchProduct`() = runTest {
        val mockSearchModel = SearchModel(
            siteId = "MLA",
            countryDefaultTimeZone = "GMT-3",
            query = "mockQuery",
            paging = PagingModel(
                total = 100,
                primaryResults = 20,
                offset = 0,
                limit = 50
            ),
            results = listOf(
                ResultModel(
                    id = "1",
                    title = "Mock Title",
                    permalink = "http://example.com",
                    thumbnail = "http://example.com/image.jpg",
                    price = 1000.0,
                    originalPrice = 1200.0,
                    shipping = ShippingModel(freeShipping = true),
                    installments = InstallmentsModel(
                        quantity = 12,
                        amount = 100.0,
                        rate = 5,
                        currencyId = "COP"
                    )
                )
            )
        )

        Mockito.`when`(searchProductRepository.searchProduct("mac"))
            .thenReturn(Result.success(mockSearchModel))

        val result = searchProductUseCase.invoke("mac")
        assertTrue(result.isSuccess)

    }

}