package com.template.home.presentation

import com.template.home.domain.Product
import java.util.Locale

fun Product.toProductUi(): ProductUi = ProductUi(
    id = id,
    title = title,
    description = description,
    formattedPrice = "$${String.format(Locale.US, "%.2f", price)}",
    formattedRating = String.format(Locale.US, "%.1f", rating),
    discountBadge = if (hasDiscount) "-${discountPercentage.toInt()}%" else null,
    brand = brand,
    category = category,
    thumbnail = thumbnail
)
