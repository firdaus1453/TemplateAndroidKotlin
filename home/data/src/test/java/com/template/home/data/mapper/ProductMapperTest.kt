package com.template.home.data.mapper

import com.template.core.database.entity.ProductEntity
import com.template.home.data.dto.ProductDto
import kotlin.test.Test
import kotlin.test.assertEquals

class ProductMapperTest {

    @Test
    fun `ProductDto toProduct maps all fields correctly`() {
        val dto = ProductDto(
            id = 1,
            title = "iPhone 9",
            description = "An apple mobile which is nothing like apple",
            price = 549.0,
            discountPercentage = 12.96,
            rating = 4.69,
            brand = "Apple",
            category = "smartphones",
            thumbnail = "https://example.com/thumb.png"
        )

        val product = dto.toProduct()

        assertEquals(1, product.id)
        assertEquals("iPhone 9", product.title)
        assertEquals("An apple mobile which is nothing like apple", product.description)
        assertEquals(549.0, product.price)
        assertEquals(12.96, product.discountPercentage)
        assertEquals(4.69, product.rating)
        assertEquals("Apple", product.brand)
        assertEquals("smartphones", product.category)
        assertEquals("https://example.com/thumb.png", product.thumbnail)
    }

    @Test
    fun `Product toEntity maps all fields correctly`() {
        val product = com.template.home.domain.Product(
            id = 2,
            title = "Samsung",
            description = "Samsung Galaxy",
            price = 1249.0,
            discountPercentage = 15.46,
            rating = 4.09,
            brand = "Samsung",
            category = "smartphones",
            thumbnail = "https://example.com/samsung.png"
        )

        val entity = product.toEntity()

        assertEquals(2, entity.id)
        assertEquals("Samsung", entity.title)
        assertEquals("Samsung Galaxy", entity.description)
        assertEquals(1249.0, entity.price)
        assertEquals(15.46, entity.discountPercentage)
        assertEquals(4.09, entity.rating)
        assertEquals("Samsung", entity.brand)
        assertEquals("smartphones", entity.category)
        assertEquals("https://example.com/samsung.png", entity.thumbnail)
    }

    @Test
    fun `ProductEntity toProduct maps all fields correctly`() {
        val entity = ProductEntity(
            id = 3,
            title = "Perfume",
            description = "Royal perfume",
            price = 30.0,
            discountPercentage = 8.4,
            rating = 4.26,
            brand = "Royal",
            category = "fragrances",
            thumbnail = "https://example.com/perfume.png"
        )

        val product = entity.toProduct()

        assertEquals(3, product.id)
        assertEquals("Perfume", product.title)
        assertEquals("Royal perfume", product.description)
        assertEquals(30.0, product.price)
        assertEquals(8.4, product.discountPercentage)
        assertEquals(4.26, product.rating)
        assertEquals("Royal", product.brand)
        assertEquals("fragrances", product.category)
        assertEquals("https://example.com/perfume.png", product.thumbnail)
    }

    @Test
    fun `round trip Product to Entity and back preserves data`() {
        val original = com.template.home.domain.Product(
            id = 10,
            title = "Test Product",
            description = "Test Description",
            price = 99.99,
            discountPercentage = 5.5,
            rating = 3.8,
            brand = "TestBrand",
            category = "testing",
            thumbnail = "https://example.com/test.png"
        )

        val roundTripped = original.toEntity().toProduct()

        assertEquals(original, roundTripped)
    }
}
