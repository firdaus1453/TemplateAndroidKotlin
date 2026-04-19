package com.template.home.data.mapper

import com.template.core.database.entity.ProductEntity
import com.template.home.data.dto.ProductDto
import com.template.home.domain.Product

fun ProductDto.toProduct() = Product(
    id = id,
    title = title,
    description = description,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    brand = brand,
    category = category,
    thumbnail = thumbnail
)

fun Product.toEntity() = ProductEntity(
    id = id,
    title = title,
    description = description,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    brand = brand,
    category = category,
    thumbnail = thumbnail
)

fun ProductEntity.toProduct() = Product(
    id = id,
    title = title,
    description = description,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    brand = brand,
    category = category,
    thumbnail = thumbnail
)
