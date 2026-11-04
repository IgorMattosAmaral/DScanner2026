package com.example.dscanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.dscanner.data.local.entity.CompetitorWithProducts
import com.example.dscanner.data.local.entity.CompetitorsEntity
import com.example.dscanner.data.local.entity.ProductEditEntity
import com.example.dscanner.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompetitorsDao {

    // ===========================
    // COMPETIDORES
    // ===========================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompetitor(competitor: CompetitorsEntity): Long

    @Delete
    suspend fun deleteCompetitor(competitor: CompetitorsEntity)

    @Query("SELECT * FROM competitors ORDER BY name")
    fun listCompetitorsFlow(): Flow<List<CompetitorsEntity>>

    @Query("SELECT * FROM competitors")
    suspend fun getAllCompetitors(): List<CompetitorsEntity>

    // ===========================
    // PRODUTOS FIXOS
    // ===========================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFixedProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT COUNT(*) > 0 FROM product")
    suspend fun hasAnyProducts(): Boolean

    @Query("SELECT * FROM product ORDER BY name")
    suspend fun getAllProducts(): List<ProductEntity>

    // Flow para atualização automática da UI
    @Query("SELECT * FROM product ORDER BY name")
    fun getAllProductsFlow(): Flow<List<ProductEntity>>

    // ===========================
    // PRODUTOS EDITADOS
    // ===========================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductEdit(productEdit: ProductEditEntity)

    @Query("SELECT * FROM product_edit WHERE competitorId = :competitorId")
    suspend fun getEditedProductsForCompetitor(
        competitorId: Long
    ): List<ProductEditEntity>

    @Query("DELETE FROM product_edit WHERE competitorId = :competitorId")
    suspend fun deleteEditedProductsForCompetitor(
        competitorId: Long
    )

    @Query("DELETE FROM product_edit WHERE id = :productEditId")
    suspend fun deleteProductEditById(productEditId: Long)

    @Query("DELETE FROM product_edit WHERE id = :productId")
    suspend fun deleteProductById(productId: Long)

    // ===========================
    // RELACIONAMENTO
    // ===========================

    @Transaction
    @Query("SELECT * FROM competitors WHERE id = :competitorId")
    fun getCompetitorWithProductsFlow(
        competitorId: Long
    ): Flow<CompetitorWithProducts?>

    @Transaction
    @Query("SELECT * FROM competitors ORDER BY name")
    fun getAllCompetitorsWithProductsFlow(): Flow<List<CompetitorWithProducts>>
}