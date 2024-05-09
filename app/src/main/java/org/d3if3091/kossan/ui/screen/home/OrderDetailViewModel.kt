package org.d3if3091.kossan.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if3091.kossan.database.OrderDao
import org.d3if3091.kossan.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class OrderDetailViewModel(private val orderDao: OrderDao) : ViewModel() {
    suspend fun getOrderById(id: Long): Order? {
        return orderDao.getOrderById(id)
    }
    fun insert(customerName: String, room: String,startTime:String,endTime:String,price:String) {
        val order = Order(
            customer_name = customerName,
            room = room,
            start_time = startTime,
            end_time = endTime,
            total_price = price.toInt()
        )

        viewModelScope.launch(Dispatchers.IO) {
            orderDao.insert(order)
        }
    }
    fun update(id: Long, customerName: String, room: String,startTime:String,endTime:String,price:String) {
        val order = Order(
            id = id,
            customer_name = customerName,
            room = room,
            start_time = startTime,
            end_time = endTime,
            total_price = price.toInt()
        )

        viewModelScope.launch(Dispatchers.IO) {
            orderDao.update(order)
        }
    }
    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            orderDao.deleteById(id)
        }
    }
}