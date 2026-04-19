package com.template.home.data

import com.template.core.domain.util.DataError
import com.template.core.domain.util.EmptyResult
import com.template.core.domain.util.Result
import com.template.core.domain.util.asEmptyDataResult
import com.template.home.domain.LocalProductDataSource
import com.template.home.domain.Product
import com.template.home.domain.ProductRepository
import com.template.home.domain.RemoteProductDataSource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class OfflineFirstProductRepository(
    private val localDataSource: LocalProductDataSource,
    private val remoteDataSource: RemoteProductDataSource
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        return localDataSource.getProducts()
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return localDataSource.searchProducts(query)
    }

    override suspend fun fetchProducts(): EmptyResult<DataError> {
        return when (val result = remoteDataSource.getProducts()) {
            is Result.Error -> {
                Timber.e("Failed to fetch products from remote: ${result.error}")
                result.asEmptyDataResult()
            }
            is Result.Success -> {
                localDataSource.upsertProducts(result.data).asEmptyDataResult()
            }
        }
    }
}
