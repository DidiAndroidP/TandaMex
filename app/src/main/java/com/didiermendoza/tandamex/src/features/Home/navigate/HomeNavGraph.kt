package com.didiermendoza.tandamex.src.features.Home.navigate

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.didiermendoza.tandamex.src.core.navigation.FeatureNavGraph
import com.didiermendoza.tandamex.src.core.navigation.HomeRoute
import com.didiermendoza.tandamex.src.core.navigation.ProfileRoute // Asegúrate de tener esta ruta
import com.didiermendoza.tandamex.src.features.Home.di.HomeModule
import com.didiermendoza.tandamex.src.features.Home.presentation.screens.HomeScreen
import com.didiermendoza.tandamex.src.features.Home.presentation.viewmodels.HomeViewModel
import com.didiermendoza.tandamex.src.features.Tanda.navigation.CreateTandaRoute
import com.didiermendoza.tandamex.src.features.Tanda.navigation.TandaDetailRoute

class HomeNavGraph(
    private val homeModule: HomeModule
) : FeatureNavGraph {

    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.composable<HomeRoute> {
            val viewModel: HomeViewModel = viewModel(
                factory = homeModule.provideHomeViewModelFactory()
            )

            HomeScreen(
                viewModel = viewModel,
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
}