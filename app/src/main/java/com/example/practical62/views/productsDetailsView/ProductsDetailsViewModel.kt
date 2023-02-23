package com.example.practical62.views.productsDetailsView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practical62.AppDispatcher
import com.example.practical62.BRAND
import com.example.practical62.api.ApiService
import com.example.practical62.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val service: ApiService,
    private val appDispatcher: AppDispatcher
) : ViewModel() {
    val state = MutableStateFlow<ProductDetailsState>(ProductDetailsState.START)

    private fun loadProducts(productId: Int) = viewModelScope.launch {
        state.value = ProductDetailsState.LOADING
        withContext(appDispatcher.IO) {
            service.getProductList(BRAND).onSuccess { productList ->
                for (product in productList) {
                    if (product.id == productId) {
                        state.value = ProductDetailsState.SUCCESS(
                            product = product
                        )
                    }
                }
            }.onFailure { e ->
                state.value = ProductDetailsState.FAILURE(
                    failureMessage = e.localizedMessage!!
                )
            }
        }
    }

    fun onStart(productId: Int) {
        loadProducts(productId = productId)
    }

    fun resetState() {
        state.value = ProductDetailsState.START
    }
}

sealed class ProductDetailsState {
    object START : ProductDetailsState()
    object LOADING : ProductDetailsState()
    data class SUCCESS(
        val product: Product
    ) : ProductDetailsState()

    data class FAILURE(val failureMessage: String) : ProductDetailsState()
}