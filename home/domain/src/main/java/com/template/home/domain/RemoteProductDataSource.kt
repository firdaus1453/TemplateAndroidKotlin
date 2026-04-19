package com.template.home.domain

import com.template.core.domain.util.DataError
import com.template.core.domain.util.EmptyResult

interface RemoteProductDataSource {
    suspend fun getProducts(limit: Int = 30, skip: Int = 0): com.template.core.domain.util.Result<List<Product>, DataError.Network>
    suspend fun searchProducts(query: String): com.template.core.domain.util.Result<List<Product>, DataError.Network>
}
