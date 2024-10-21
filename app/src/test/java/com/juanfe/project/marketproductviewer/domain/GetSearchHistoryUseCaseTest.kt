package com.juanfe.project.marketproductviewer.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GetSearchHistoryUseCaseTest {

    @Mock
    private lateinit var searchProductRepository: SearchProductRepository

    private lateinit var getSearchHistoryUseCase: GetSearchHistoryUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getSearchHistoryUseCase = GetSearchHistoryUseCase(searchProductRepository)
    }

    @Test
    fun `invoke should return search history`() = runTest {
        val mockHistory = listOf("Search 1", "Search 2")

        // Simula la devolución del flujo de datos desde el repositorio
        `when`(searchProductRepository.getSearchHistoryProducts()).thenReturn(flowOf(mockHistory))

        // Llama al caso de uso
        val result = getSearchHistoryUseCase.invoke().first()

        // Verifica que el resultado sea el esperado
        assertEquals(mockHistory, result)
    }
}
