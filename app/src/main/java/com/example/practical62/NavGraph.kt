package com.example.practical62

const val HOME_ROUTE = "ProductsView"
const val BASE_URL = "http://makeup-api.herokuapp.com/"
const val BRAND = "maybelline"

sealed class UserNavigation(val route: String) {
    object DetailsView : UserNavigation("ProductDetails")
}