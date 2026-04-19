package com.template.home.data

import com.template.core.data.networking.get
import com.template.core.domain.util.DataError
import com.template.core.domain.util.Result
import com.template.core.domain.util.map
import com.template.home.data.dto.ProductsResponse
import com.template.home.data.mapper.toProduct
import com.template.home.domain.Product
import com.template.home.domain.RemoteProductDataSource
import io.ktor.client.HttpClient

class KtorRemoteProductDataSource(
    private val httpClient: HttpClient
) : RemoteProductDataSource {

    override suspend fun getProducts(
        limit: Int,
        skip: Int
    ): Result<List<Product>, DataError.Network> {
        return httpClient.get<ProductsResponse>(
            route = "/products",
            queryParameters = mapOf(
                "limit" to limit,
                "skip" to skip
            )
        ).map { response ->
            response.products.map { it.toProduct() }
        }
    }

    override suspend fun searchProducts(
        query: String
    ): Result<List<Product>, DataError.Network> {
        return httpClient.get<ProductsResponse>(
            route = "/products/search",
            queryParameters = mapOf("q" to query)
        ).map { response ->
            response.products.map { it.toProduct() }
        }
    }
}
