package com.example.practical62.views.productsDetailsView

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.practical62.R
import com.example.practical62.alerter.showBanner
import com.example.practical62.model.Product
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig

@Composable
fun ProductsDetailsView(productId: Int) {
    val viewModel = hiltViewModel<ProductDetailsViewModel>()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        viewModel.onStart(productId)
    })
    val state by viewModel.state.collectAsState()
    state.let {
        when (it) {
            ProductDetailsState.LOADING -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(color = colorResource(id = R.color.purple_200))
                }
            }
            is ProductDetailsState.SUCCESS -> {
                ProductDetailsCardView(it.product)
            }
            is ProductDetailsState.FAILURE -> {
                val message = it.failureMessage
                LaunchedEffect(key1 = message) {
                    showBanner(context, message)
                    viewModel.resetState()
                }
            }
        }
    }
}

@Composable
fun ProductDetailsCardView(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = rememberImagePainter(data = product.imageLink,
                builder = {
                    transformations(CircleCropTransformation())
                    crossfade(500)
                }),
            contentDescription = "Product Image",
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = product.name,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(4.dp),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Description:")
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily.SansSerif,
                    )
                ) {
                    append(product.description)
                }
            },
            textAlign = TextAlign.Start,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(end = 30.dp, top = 10.dp)
        )
        var rating by remember { mutableStateOf(product.rating.toFloat()) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("Price: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Rs.${product.price}")
                    }
                },
                textAlign = TextAlign.Center
            )

            RatingBar(value = rating,
                config = RatingBarConfig()
                    .isIndicator(true)
                    .inactiveColor(Color(0, 0, 0, 13)),
                onValueChange = {
                    rating = it
                }, onRatingChanged = {
                    rating = it
                })
        }

    }
}
