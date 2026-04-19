package com.template.home.presentation

data class ProductUi(
    val id: Int,
    val title: String,
    val description: String,
    val formattedPrice: String,
    val formattedRating: String,
    val discountBadge: String?,
    val brand: String,
    val category: String,
    val thumbnail: String
)
