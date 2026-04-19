package com.template.home.presentation

import com.template.core.domain.util.DataError
import com.template.core.domain.util.EmptyResult
import com.template.core.domain.util.Result
import com.template.home.domain.Product
import com.template.home.domain.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeProductRepository : ProductRepository {
    var shouldReturnError = false
    var errorToReturn: DataError = DataError.Network.SERVER_ERROR

    private val productsFlow = MutableSharedFlow<List<Product>>(replay = 1)
    private val searchFlow = MutableSharedFlow<List<Product>>(replay = 1)
    var fetchCallCount = 0
        private set

    init {
        productsFlow.tryEmit(emptyList())
    }

    suspend fun emitProducts(products: List<Product>) {
        productsFlow.emit(products)
    }

    suspend fun emitSearchResults(products: List<Product>) {
        searchFlow.emit(products)
    }

    override fun getProducts(): Flow<List<Product>> = productsFlow

    override fun searchProducts(query: String): Flow<List<Product>> = searchFlow

    override suspend fun fetchProducts(): EmptyResult<DataError> {
        fetchCallCount++
        return if (shouldReturnError) {
            Result.Error(errorToReturn)
        } else {
            Result.Success(Unit)
        }
    }

    companion object {
        fun createSampleProducts() = listOf(
            Product(
                id = 1,
                title = "iPhone 9",
                description = "An apple mobile",
                price = 549.0,
                discountPercentage = 12.96,
                rating = 4.69,
                brand = "Apple",
                category = "smartphones",
                thumbnail = "https://example.com/thumb1.png"
            ),
            Product(
                id = 2,
                title = "Samsung Universe 9",
                description = "Samsung Galaxy",
                price = 1249.0,
                discountPercentage = 15.46,
                rating = 4.09,
                brand = "Samsung",
                category = "smartphones",
                thumbnail = "https://example.com/thumb2.png"
            )
        )
    }
}
