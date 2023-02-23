package com.example.practical62

import com.example.practical62.api.ApiService
import com.example.practical62.model.Product
import com.example.practical62.views.productsDetailsView.ProductDetailsState
import com.example.practical62.views.productsDetailsView.ProductDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsDetailsViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private val services = mock<ApiService>()
    private val testDispatcher = AppDispatcher(
        IO = TestCoroutineDispatcher()
    )
    private lateinit var viewModel: ProductDetailsViewModel

    @Before
    fun setup() {
        initViewModel()
    }

    private fun initViewModel() {
        viewModel =
            ProductDetailsViewModel(
                services,
                testDispatcher
            )
    }

    companion object {
        val product = Product(
            495,
            "maybelline",
            "Maybelline Face Studio Master Hi-Light Light Booster Bronzer",
            "14.99",
            null,
            null,
            "https://d3t32hsnjxo7q6.cloudfront.net/i/991799d3e70b8856686979f8ff6dcfe0_ra,w158,h184_pa,w158,h184.png",
            "https://well.ca/products/maybelline-face-studio-master_88837.html",
            "https://well.ca",
            "Maybelline Face Studio Master Hi-Light Light Boosting bronzer formula has an expert balance of shade + shimmer illuminator for natural glow. Skin goes soft-lit with zero glitz. For Best Results: Brush over all shades in palette and gently sweep over cheekbones, brow bones, and temples, or anywhere light naturally touches the face. ",
            5.0,
            null,
            "bronzer",
            null,
            "2016-10-01T18:36:15.012Z",
            "2017-12-23T21:08:50.624Z",
            "http://makeup-api.herokuapp.com/api/v1/products/495.json",
            "//s3.amazonaws.com/donovanbailey/products/api_featured_images/000/000/495/original/open-uri20171223-4-9hrto4?1514063330",
            null
        )

    }

    @Test
    fun test_loadProduct() = runBlocking {
        val loadingData = flowOf(ProductDetailsState.LOADING)
        whenever(services.getProductList("maybelline")).doSuspendableAnswer {
            withContext(Dispatchers.IO) { delay(5000) }
            Result.success(emptyList())
        }
        initViewModel()
        viewModel.onStart(product.id)
        Assert.assertEquals(loadingData.first(), viewModel.state.value)
        whenever(services.getProductList("maybelline")).thenReturn(Result.success(listOf(product)))
        initViewModel()
        viewModel.onStart(product.id)
        val successState = flowOf(ProductDetailsState.SUCCESS(product))
        Assert.assertEquals(successState.first(), viewModel.state.value)
    }

    @Test
    fun test_resetState_onReconnect_to_Network() = runBlocking {
        val startState = flowOf(ProductDetailsState.START)
        viewModel.resetState()
        Assert.assertEquals(startState.first(), viewModel.state.value)
    }
}