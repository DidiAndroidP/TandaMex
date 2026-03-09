package com.didiermendoza.tandamex.src.features.Tanda.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.didiermendoza.tandamex.src.core.navigation.HomeRoute
import com.didiermendoza.tandamex.src.features.Tanda.presentation.screens.CreateTandaScreen
import com.didiermendoza.tandamex.src.features.Tanda.presentation.screens.TandaScreen
import com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels.CreateTandaViewModel
import com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels.TandaViewModel
import kotlinx.serialization.Serializable

@Serializable
data class TandaDetailRoute(val tandaId: Int)

@Serializable
object CreateTandaRoute

fun NavGraphBuilder.tandaNavGraph(navController: NavHostController) {
    composable<TandaDetailRoute> { backStackEntry ->
        val route: TandaDetailRoute = backStackEntry.toRoute()

        val viewModel: TandaViewModel = hiltViewModel()

        if (viewModel.tanda.value == null) {
            viewModel.loadTandaDetail(route.tandaId)
        }

        TandaScreen(
            viewModel = viewModel,
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<CreateTandaRoute> {
        val viewModel: CreateTandaViewModel = hiltViewModel()

        CreateTandaScreen(
            viewModel = viewModel,
            onBackClick = { navController.popBackStack() },
            onSuccess = {
                navController.navigate(HomeRoute) {
                    popUpTo(HomeRoute) { inclusive = true }
                }
            }
        )
    }
}