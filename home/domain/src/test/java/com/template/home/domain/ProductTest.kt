package com.template.home.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProductTest {

    @Test
    fun `discountedPrice calculates correctly`() {
        val product = Product(
            id = 1,
            title = "Test",
            description = "Desc",
            price = 100.0,
            discountPercentage = 20.0,
            rating = 4.0,
            brand = "Brand",
            category = "Cat",
            thumbnail = ""
        )
        assertEquals(80.0, product.discountedPrice, 0.01)
    }

    @Test
    fun `discountedPrice with zero discount equals price`() {
        val product = Product(
            id = 1,
            title = "Test",
            description = "Desc",
            price = 549.0,
            discountPercentage = 0.0,
            rating = 4.0,
            brand = "Brand",
            category = "Cat",
            thumbnail = ""
        )
        assertEquals(549.0, product.discountedPrice, 0.01)
    }

    @Test
    fun `hasDiscount returns true when discount is positive`() {
        val product = Product(
            id = 1, title = "T", description = "D",
            price = 100.0, discountPercentage = 5.0,
            rating = 3.0, brand = "B", category = "C", thumbnail = ""
        )
        assertTrue(product.hasDiscount)
    }

    @Test
    fun `hasDiscount returns false when discount is zero`() {
        val product = Product(
            id = 1, title = "T", description = "D",
            price = 100.0, discountPercentage = 0.0,
            rating = 3.0, brand = "B", category = "C", thumbnail = ""
        )
        assertFalse(product.hasDiscount)
    }
}
