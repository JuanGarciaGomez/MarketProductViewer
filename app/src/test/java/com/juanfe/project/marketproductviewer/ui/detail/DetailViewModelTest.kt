package com.juanfe.project.marketproductviewer.ui.detail

import app.cash.turbine.test
import com.juanfe.project.marketproductviewer.domain.InstallmentsModel
import com.juanfe.project.marketproductviewer.domain.ResultModel
import com.juanfe.project.marketproductviewer.domain.ShippingModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        viewModel = DetailViewModel()
    }

    @Test
    fun `set product`() = runTest {
        val product = ResultModel(
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

        viewModel.viewState.test {
            viewModel.setProduct(product)

            assertEquals(DetailViewState.Success(null),awaitItem())
            assertEquals(DetailViewState.Success(product),awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

    }


}