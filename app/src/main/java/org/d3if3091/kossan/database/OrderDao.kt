package org.d3if3091.kossan.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3091.kossan.model.Order

@Dao
interface OrderDao {

    @Insert
    suspend fun insert(catatan: Order)

    @Update
    suspend fun update(catatan: Order)

    @Query("SELECT * FROM `order` ORDER BY end_time DESC")
    fun getAllOrder(): Flow<List<Order>>

    @Query("SELECT * FROM `order` WHERE id = :id")
    suspend fun getOrderById(id: Long): Order?
//
    @Query("DELETE FROM `order` WHERE id = :id")
    suspend fun deleteById(id: Long)
}