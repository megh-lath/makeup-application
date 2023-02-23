package com.example.practical62

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practical62.ui.theme.Practical62Theme
import com.example.practical62.views.productsDetailsView.ProductsDetailsView
import com.example.practical62.views.productsView.ProductsView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practical62Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainView()
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainView() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = HOME_ROUTE, builder = {
        composable(route = HOME_ROUTE) {
            ProductsView(navController = navController)
        }
        composable(route = UserNavigation.DetailsView.route + "/{productId}") { navBackStack ->
            val productId = navBackStack.arguments?.getString("productId")
                ?: throw AssertionError(" ")
            ProductsDetailsView(productId = productId.toInt())
        }
    })
}