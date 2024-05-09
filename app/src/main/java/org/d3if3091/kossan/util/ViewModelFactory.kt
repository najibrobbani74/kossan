package org.d3if3091.kossan.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3091.kossan.database.OrderDao
import org.d3if3091.kossan.ui.screen.home.HomeViewModel
import org.d3if3091.kossan.ui.screen.home.OrderDetailViewModel

class ViewModelFactory(private val dao: OrderDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dao) as T
        }
        else if (modelClass.isAssignableFrom(OrderDetailViewModel::class.java)) {
            return OrderDetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}