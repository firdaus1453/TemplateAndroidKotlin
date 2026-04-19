package com.template.home.domain

import com.template.core.domain.util.DataError
import com.template.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun fetchProducts(): EmptyResult<DataError>
}
