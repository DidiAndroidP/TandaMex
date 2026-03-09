package com.didiermendoza.tandamex.src.features.Home.navigate

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.didiermendoza.tandamex.src.core.navigation.HomeRoute
import com.didiermendoza.tandamex.src.core.navigation.ProfileRoute
import com.didiermendoza.tandamex.src.features.Home.presentation.screens.HomeScreen
import com.didiermendoza.tandamex.src.features.Tanda.navigation.CreateTandaRoute
import com.didiermendoza.tandamex.src.features.Tanda.navigation.TandaDetailRoute

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    composable<HomeRoute> {
        HomeScreen(
            onNavigateToCreateTanda = {
                navController.navigate(CreateTandaRoute)
            },
            onNavigateToDetail = { tandaId ->
                navController.navigate(TandaDetailRoute(tandaId))
            },
            onNavigateToProfile = {
                navController.navigate(ProfileRoute)
            }
        )
    }
}