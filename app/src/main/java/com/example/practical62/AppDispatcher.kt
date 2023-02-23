package com.example.practical62

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class AppDispatcher(
    val IO: CoroutineDispatcher = Dispatchers.IO,
    val MAIN: CoroutineDispatcher = Dispatchers.Main,
)
