package org.d3if3091.kossan.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3if3091.kossan.ui.screen.home.HomeScreen
import org.d3if3091.kossan.ui.screen.home.KEY_ID_ORDER
import org.d3if3091.kossan.ui.screen.home.OrderDetailScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navHostController: NavHostController = rememberNavController()){
    NavHost(navController = navHostController, startDestination = Screen.Home.route){
        composable(route = Screen.Home.route){
            HomeScreen(navHostController = navHostController)
        }
        composable(route = Screen.AddOrder.route){
            OrderDetailScreen(navHostController = navHostController)
        }
        composable(
            route = Screen.UpdateOrder.route,
            arguments = listOf(
                navArgument(KEY_ID_ORDER) { type = NavType.LongType }
            )
        ) {navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_ORDER)
            OrderDetailScreen(navHostController, id)
        }

    }
}