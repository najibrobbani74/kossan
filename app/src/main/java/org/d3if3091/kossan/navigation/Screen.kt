package org.d3if3091.kossan.navigation

import org.d3if3091.kossan.ui.screen.home.KEY_ID_ORDER

sealed class Screen(val route:String) {
    data object Home: Screen("homeScreen")
    data object AddOrder: Screen("addOrderScreen")
    data object UpdateOrder: Screen("updateOrderScreen/{$KEY_ID_ORDER}"){
        fun withId(id: Long) = "updateOrderScreen/$id"
    }
}