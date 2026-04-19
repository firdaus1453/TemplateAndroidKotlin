package com.template.home.data

import com.template.core.database.dao.ProductDao
import com.template.core.domain.util.DataError
import com.template.core.domain.util.Result
import com.template.home.data.mapper.toEntity
import com.template.home.data.mapper.toProduct
import com.template.home.domain.LocalProductDataSource
import com.template.home.domain.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RoomLocalProductDataSource(
    private val productDao: ProductDao
) : LocalProductDataSource {

    override fun getProducts(): Flow<List<Product>> {
        return productDao.getAll().map { entities ->
            entities.map { it.toProduct() }
        }
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.search(query).map { entities ->
            entities.map { it.toProduct() }
        }
    }

    override suspend fun upsertProducts(
        products: List<Product>
    ): Result<Unit, DataError.Local> {
        return try {
            productDao.upsertAll(products.map { it.toEntity() })
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to upsert products")
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteAll(): Result<Unit, DataError.Local> {
        return try {
            productDao.deleteAll()
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete all products")
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
