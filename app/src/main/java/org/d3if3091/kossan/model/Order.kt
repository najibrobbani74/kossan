package org.d3if3091.kossan.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val customer_name: String,
    val room: String,
    val start_time: String,
    val end_time: String,
    val total_price: Int,
)
