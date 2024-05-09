package org.d3if3091.kossan.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3091.kossan.database.OrderDao
import org.d3if3091.kossan.model.Order

class HomeViewModel(orderDao: OrderDao) : ViewModel() {

    val data: StateFlow<List<Order>> = orderDao.getAllOrder().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}