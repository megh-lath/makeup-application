package com.example.practical62.views.productsView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practical62.AppDispatcher
import com.example.practical62.api.ApiService
import com.example.practical62.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val service: ApiService,
    private val appDispatcher: AppDispatcher
) : ViewModel() {
    val state = MutableStateFlow<ProductState>(ProductState.START)

    private fun loadProducts() = viewModelScope.launch {
        state.value = ProductState.LOADING
        withContext(appDispatcher.IO) {
            service.getProductList("maybelline").onSuccess {
                state.value = ProductState.SUCCESS(
                    productList = it
                )
            }.onFailure { e ->
                state.value = ProductState.FAILURE(
                    failureMessage = e.localizedMessage!!
                )
            }
        }
    }

    fun onStart() {
        loadProducts()
    }

    fun resetState() {
        state.value = ProductState.START
    }
}

sealed class ProductState {
    object START : ProductState()
    object LOADING : ProductState()
    data class SUCCESS(
        val productList: List<Product>
    ) : ProductState()

    data class FAILURE(val failureMessage: String) : ProductState()
}