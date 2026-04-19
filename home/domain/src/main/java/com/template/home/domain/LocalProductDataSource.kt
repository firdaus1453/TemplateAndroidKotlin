package com.template.home.domain

import com.template.core.domain.util.DataError
import com.template.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface LocalProductDataSource {
    fun getProducts(): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun upsertProducts(products: List<Product>): Result<Unit, DataError.Local>
    suspend fun deleteAll(): Result<Unit, DataError.Local>
}
