package com.template.home.domain

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val brand: String,
    val category: String,
    val thumbnail: String
) {
    val discountedPrice: Double
        get() = price * (1 - discountPercentage / 100.0)

    val hasDiscount: Boolean
        get() = discountPercentage > 0.0
}
