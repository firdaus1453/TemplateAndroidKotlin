package com.template.home.data

import com.template.core.domain.util.DataError
import com.template.core.domain.util.Result
import com.template.home.domain.LocalProductDataSource
import com.template.home.domain.Product
import com.template.home.domain.RemoteProductDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Tests for [OfflineFirstProductRepository].
 *
 * This validates the offline-first repository pattern:
 * 1. getProducts() delegates to local data source (Room)
 * 2. searchProducts() delegates to local data source
 * 3. fetchProducts() success → upserts into local (offline-first sync)
 * 4. fetchProducts() error → returns error without touching local
 */
class OfflineFirstProductRepositoryTest {

    // ── Fakes ──

    private class FakeLocalProductDataSource : LocalProductDataSource {
        private val productsFlow = MutableStateFlow<List<Product>>(emptyList())
        private val searchFlow = MutableStateFlow<List<Product>>(emptyList())
        var upsertedProducts: List<Product>? = null
            private set
        var upsertCallCount = 0
            private set
        var deleteAllCallCount = 0
            private set
        var shouldReturnError = false

        override fun getProducts(): Flow<List<Product>> = productsFlow
        override fun searchProducts(query: String): Flow<List<Product>> = searchFlow

        override suspend fun upsertProducts(products: List<Product>): Result<Unit, DataError.Local> {
            upsertCallCount++
            return if (shouldReturnError) {
                Result.Error(DataError.Local.DISK_FULL)
            } else {
                upsertedProducts = products
                productsFlow.value = products
                Result.Success(Unit)
            }
        }

        override suspend fun deleteAll(): Result<Unit, DataError.Local> {
            deleteAllCallCount++
            productsFlow.value = emptyList()
            return Result.Success(Unit)
        }
    }

    private class FakeRemoteProductDataSource : RemoteProductDataSource {
        var shouldReturnError = false
        var errorToReturn: DataError.Network = DataError.Network.SERVER_ERROR
        var products: List<Product> = emptyList()
        var getProductsCallCount = 0
            private set
        var lastLimit: Int? = null
            private set
        var lastSkip: Int? = null
            private set

        override suspend fun getProducts(
            limit: Int,
            skip: Int
        ): Result<List<Product>, DataError.Network> {
            getProductsCallCount++
            lastLimit = limit
            lastSkip = skip
            return if (shouldReturnError) {
                Result.Error(errorToReturn)
            } else {
                Result.Success(products)
            }
        }

        override suspend fun searchProducts(query: String): Result<List<Product>, DataError.Network> {
            return Result.Success(emptyList())
        }
    }

    // ── Test Setup ──

    private val fakeLocal = FakeLocalProductDataSource()
    private val fakeRemote = FakeRemoteProductDataSource()
    private val repository = OfflineFirstProductRepository(fakeLocal, fakeRemote)

    private val sampleProducts = listOf(
        Product(id = 1, title = "iPhone", description = "Phone", price = 999.0,
            discountPercentage = 5.0, rating = 4.5, brand = "Apple",
            category = "phones", thumbnail = "https://img.com/1.png"),
        Product(id = 2, title = "Galaxy", description = "Phone", price = 899.0,
            discountPercentage = 10.0, rating = 4.2, brand = "Samsung",
            category = "phones", thumbnail = "https://img.com/2.png")
    )

    // ── getProducts() tests ──

    @Test
    fun `getProducts delegates to local data source`() {
        // getProducts() should return the local flow directly
        val flow = repository.getProducts()
        assertEquals(fakeLocal.getProducts(), flow)
    }

    // ── searchProducts() tests ──

    @Test
    fun `searchProducts delegates to local data source`() {
        val flow = repository.searchProducts("test")
        assertEquals(fakeLocal.searchProducts("test"), flow)
    }

    // ── fetchProducts() success path ──

    @Test
    fun `fetchProducts success upserts remote data into local`() = runTest {
        fakeRemote.products = sampleProducts

        val result = repository.fetchProducts()

        assertIs<Result.Success<Unit>>(result)
        assertEquals(1, fakeLocal.upsertCallCount)
        assertEquals(sampleProducts, fakeLocal.upsertedProducts)
    }

    @Test
    fun `fetchProducts success calls remote with default params`() = runTest {
        fakeRemote.products = emptyList()

        repository.fetchProducts()

        assertEquals(1, fakeRemote.getProductsCallCount)
        assertEquals(30, fakeRemote.lastLimit)   // default limit
        assertEquals(0, fakeRemote.lastSkip)     // default skip
    }

    // ── fetchProducts() error paths ──

    @Test
    fun `fetchProducts remote error returns error without upserting`() = runTest {
        fakeRemote.shouldReturnError = true
        fakeRemote.errorToReturn = DataError.Network.NO_INTERNET

        val result = repository.fetchProducts()

        assertIs<Result.Error<DataError>>(result)
        assertEquals(0, fakeLocal.upsertCallCount)
    }

    @Test
    fun `fetchProducts remote server error propagates correct error type`() = runTest {
        fakeRemote.shouldReturnError = true
        fakeRemote.errorToReturn = DataError.Network.SERVER_ERROR

        val result = repository.fetchProducts()

        assertIs<Result.Error<DataError>>(result)
    }

    @Test
    fun `fetchProducts local upsert failure returns error`() = runTest {
        fakeRemote.products = sampleProducts
        fakeLocal.shouldReturnError = true

        val result = repository.fetchProducts()

        assertIs<Result.Error<DataError>>(result)
    }
}
