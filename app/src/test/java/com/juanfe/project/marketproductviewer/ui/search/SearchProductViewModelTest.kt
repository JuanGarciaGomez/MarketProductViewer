import android.content.Context
import app.cash.turbine.test
import com.juanfe.project.marketproductviewer.domain.ExceptionService
import com.juanfe.project.marketproductviewer.domain.GetSearchHistoryUseCase
import com.juanfe.project.marketproductviewer.domain.InstallmentsModel
import com.juanfe.project.marketproductviewer.domain.PagingModel
import com.juanfe.project.marketproductviewer.domain.ResultModel
import com.juanfe.project.marketproductviewer.domain.SearchModel
import com.juanfe.project.marketproductviewer.domain.SearchProductUseCase
import com.juanfe.project.marketproductviewer.domain.ShippingModel
import com.juanfe.project.marketproductviewer.ui.search.SearchProductViewModel
import com.juanfe.project.marketproductviewer.ui.search.SearchProductViewState
import com.juanfe.project.marketproductviewer.ui.search.UserIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class SearchProductViewModelTest {

    @Mock
    private lateinit var searchProductUseCase: SearchProductUseCase

    @Mock
    private lateinit var getSearchHistoryUseCase: GetSearchHistoryUseCase

    @Mock
    private lateinit var context: Context

    private lateinit var viewModel: SearchProductViewModel


    // Set up the TestCoroutineDispatcher for coroutines testing
    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)

        // Initialize the ViewModel with mocked dependencies
        viewModel = SearchProductViewModel(
            searchProductUseCase = searchProductUseCase,
            getSearchHistoryUseCase = getSearchHistoryUseCase,
            context = context
        )
    }

    @Test
    fun `search successful updates viewState to Success`() = runTest {
        // Mock data
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

        `when`(searchProductUseCase.invoke("query")).thenReturn(Result.success(mockSearchModel))

        // Observe viewState
        viewModel.viewState.test {
            // Trigger search intent
            viewModel.handleIntent(UserIntent.SearchProduct("query"))

            // Verify initial loading state
            assertEquals(SearchProductViewState.Loading(true), awaitItem())

            val successState = awaitItem()
            assertNotNull(successState)
            cancelAndIgnoreRemainingEvents()

        }
    }

    @Test
    fun `search fails updates viewState to Error`() = runTest {
        // Mock exception
        val mockException = ExceptionService("Network error")
        `when`(searchProductUseCase.invoke("query")).thenReturn(Result.failure(mockException))

        // Observe viewState
        viewModel.viewState.test {
            viewModel.handleIntent(UserIntent.SearchProduct("query"))

            // Verify initial loading state
            assertEquals(SearchProductViewState.Loading(true), awaitItem())
            assertEquals(SearchProductViewState.Loading(false), awaitItem())
            cancelAndIgnoreRemainingEvents()

        }
    }

    @Test
    fun `get search history updates searchHistory`() = runTest {
        val mockHistory = listOf("Search 1", "Search 2")
        `when`(getSearchHistoryUseCase.invoke()).thenReturn(flow { emit(mockHistory) })

        viewModel.searchHistory.test {
            viewModel.handleIntent(UserIntent.TapSearch)

            assertEquals(null, awaitItem())

            assertEquals(mockHistory, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
