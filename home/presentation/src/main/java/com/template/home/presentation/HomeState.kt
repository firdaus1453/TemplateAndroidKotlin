package com.template.home.presentation

data class HomeState(
    val products: List<ProductUi> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)
