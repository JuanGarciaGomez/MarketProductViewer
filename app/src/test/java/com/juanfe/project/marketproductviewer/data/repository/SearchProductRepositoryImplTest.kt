import android.content.Context
import com.juanfe.project.marketproductviewer.R
import com.juanfe.project.marketproductviewer.data.local.DataStoreManager
import com.juanfe.project.marketproductviewer.data.network.ProductService
import com.juanfe.project.marketproductviewer.data.network.response.InstallmentsResponse
import com.juanfe.project.marketproductviewer.data.network.response.PagingResponse
import com.juanfe.project.marketproductviewer.data.network.response.ResultResponse
import com.juanfe.project.marketproductviewer.data.network.response.SearchResponse
import com.juanfe.project.marketproductviewer.data.network.response.ShippingResponse
import com.juanfe.project.marketproductviewer.data.network.toDomain
import com.juanfe.project.marketproductviewer.data.repository.SearchProductRepositoryImpl
import com.juanfe.project.marketproductviewer.domain.ExceptionService
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class SearchProductRepositoryImplTest {

    @Mock
    private lateinit var productService: ProductService

    @Mock
    private lateinit var dataStoreManager: DataStoreManager

    @Mock
    private lateinit var context: Context

    private lateinit var searchProductRepositoryImpl: SearchProductRepositoryImpl

    private val mockSearchModel = SearchResponse(
        siteId = "MLA",
        countryDefaultTimeZone = "GMT-3",
        query = "mockQuery",
        paging = PagingResponse(
            total = 100,
            primaryResults = 20,
            offset = 0,
            limit = 50
        ),
        results = listOf(
            ResultResponse(
                id = "1",
                title = "Mock Title",
                permalink = "http://example.com",
                thumbnail = "http://example.com/image.jpg",
                price = 1000.0,
                originalPrice = 1200.0,
                shipping = ShippingResponse(freeShipping = true),
                installments = InstallmentsResponse(
                    quantity = 12,
                    amount = 100.0,
                    rate = 5,
                    currencyId = "COP"
                )
            )
        )
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(context.getString(R.string.unknown_error)).thenReturn("Error Inesperado")
        searchProductRepositoryImpl =
            SearchProductRepositoryImpl(productService, dataStoreManager, context)
    }

    @Test
    fun `test searchProduct success`() = runTest {
        val query = "example"

        val response = Response.success(mockSearchModel)

        `when`(productService.search(query)).thenReturn(response)
        `when`(dataStoreManager.allHistory).thenReturn(flowOf(""))


        val result = searchProductRepositoryImpl.searchProduct(query)

        assertTrue(result.isSuccess)
        assertEquals(mockSearchModel.toDomain(), result.getOrNull())
        verify(dataStoreManager).saveSearch(query)
    }

    @Test
    fun `test searchProduct error`() = runTest {
        val history = "query1,query2,query3"
        val query = "example"

        `when`(productService.search(query)).thenThrow(RuntimeException("Error Inesperado"))
        `when`(dataStoreManager.allHistory).thenReturn(flowOf(history))

        val result = searchProductRepositoryImpl.searchProduct(query)

        assertTrue(result.isFailure)
        assertEquals("Error Inesperado", (result.exceptionOrNull() as ExceptionService).msgError)
    }


    @Test
    fun `test getSearchHistoryProducts returns correct list`() = runTest {
        val history = "query1,query2,query3"
        `when`(dataStoreManager.allHistory).thenReturn(flowOf(history))

        val result = searchProductRepositoryImpl.getSearchHistoryProducts().first()

        assertEquals(listOf("query1", "query2", "query3"), result)
    }
}
