package com.template.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.template.core.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<ProductEntity>>

    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Upsert
    suspend fun upsert(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}
