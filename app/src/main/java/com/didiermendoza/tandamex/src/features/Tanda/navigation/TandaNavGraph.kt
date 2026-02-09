package com.didiermendoza.tandamex.src.features.Tanda.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.didiermendoza.tandamex.src.core.navigation.FeatureNavGraph
import com.didiermendoza.tandamex.src.core.navigation.HomeRoute
import com.didiermendoza.tandamex.src.features.Tanda.di.TandaModule
import com.didiermendoza.tandamex.src.features.Tanda.presentation.screens.CreateTandaScreen
import com.didiermendoza.tandamex.src.features.Tanda.presentation.screens.TandaScreen
import com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels.CreateTandaViewModel
import com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels.TandaViewModel
import kotlinx.serialization.Serializable

@Serializable
data class TandaDetailRoute(val tandaId: Int)

@Serializable
object CreateTandaRoute

class TandaNavGraph(
    private val tandaModule: TandaModule
) : FeatureNavGraph {

    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.composable<TandaDetailRoute> { backStackEntry ->
            val route: TandaDetailRoute = backStackEntry.toRoute()

            val viewModel: TandaViewModel = viewModel(
                factory = tandaModule.provideTandaViewModelFactory()
            )

            if (viewModel.tanda.value == null) {
                viewModel.loadTandaDetail(route.tandaId)
            }

            TandaScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        navGraphBuilder.composable<CreateTandaRoute> {
            val viewModel: CreateTandaViewModel = viewModel(
                factory = tandaModule.provideTandaViewModelFactory()
            )

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
}