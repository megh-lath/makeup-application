package com.example.practical62.views.productsView

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.practical62.R
import com.example.practical62.UserNavigation
import com.example.practical62.alerter.showBanner
import com.example.practical62.model.Product
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig

@Composable
fun ProductsView(navController: NavController) {
    val viewModel = hiltViewModel<ProductsViewModel>()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        viewModel.onStart()
    })
    val state by viewModel.state.collectAsState()
    state.let {
        when (it) {
            ProductState.LOADING -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(color = colorResource(id = R.color.purple_200))
                }
            }
            is ProductState.SUCCESS -> {
                val products = it.productList
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    itemsIndexed(products) { _, item ->
                        ProductsCardView(product = item, navController = navController)
                    }
                }
            }
            is ProductState.FAILURE -> {
                val message = it.failureMessage
                LaunchedEffect(key1 = message) {
                    showBanner(context, message)
                    viewModel.resetState()
                }
            }
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ProductsCardView(product: Product, navController: NavController) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
    ) {
        Row(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clickable(
                    enabled = true,
                    onClick = {
                        navController.navigate(UserNavigation.DetailsView.route + "/${product.id}")
                    }
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .align(Alignment.CenterVertically)
            )
            {
                Image(
                    painter = rememberImagePainter(data = product.imageLink,
                        builder = {
                            transformations(CircleCropTransformation())
                            crossfade(500)
                        }),
                    contentDescription = "Product Image",
                    modifier = Modifier.size(90.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .weight(3f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.description,
                    modifier = Modifier.padding(4.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                var rating by remember { mutableStateOf(product.rating.toFloat()) }
                RatingBar(value = rating,
                    modifier = Modifier.padding(4.dp),
                    config = RatingBarConfig()
                        .isIndicator(true)
                        .size(15.dp)
                        .inactiveColor(Color(0, 0, 0, 13)),
                    onValueChange = {
                        rating = it
                    }, onRatingChanged = {
                        rating = it
                    })

            }
        }
    }
}

