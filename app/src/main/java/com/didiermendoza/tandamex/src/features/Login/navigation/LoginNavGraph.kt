package com.didiermendoza.tandamex.src.features.Login.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
            val state by viewModel.uiState.collectAsState()

            LaunchedEffect(state.successData) {
                if (state.successData != null) {
                    navController.navigate(HomeRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                    viewModel.resetState()
                }
            }
            LoginScreen(
            )
        }
    }
}