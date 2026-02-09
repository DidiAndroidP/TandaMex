package com.didiermendoza.tandamex.src.features.Login.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.didiermendoza.tandamex.src.core.navigation.FeatureNavGraph
import com.didiermendoza.tandamex.src.core.navigation.LoginRoute
import com.didiermendoza.tandamex.src.core.navigation.HomeRoute
import com.didiermendoza.tandamex.src.core.navigation.RegisterRoute
import com.didiermendoza.tandamex.src.features.Login.di.LoginModule
import com.didiermendoza.tandamex.src.features.Login.presentation.screens.LoginScreen
import com.didiermendoza.tandamex.src.features.Login.presentation.viewmodels.LoginViewModel

class LoginNavGraph(
    private val loginModule: LoginModule
) : FeatureNavGraph {

    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {

        navGraphBuilder.composable<LoginRoute> {
            val viewModel: LoginViewModel = viewModel(
                factory = loginModule.provideLoginViewModelFactory()
            )

            LoginScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate(HomeRoute) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(RegisterRoute)
                }
            )
        }
    }
}