package com.didiermendoza.tandamex.src.features.Login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.didiermendoza.tandamex.src.core.navigation.LoginRoute
import com.didiermendoza.tandamex.src.core.navigation.HomeRoute
import com.didiermendoza.tandamex.src.core.navigation.RegisterRoute
import com.didiermendoza.tandamex.src.features.Login.presentation.screens.LoginScreen

fun NavGraphBuilder.loginNavGraph(navController: NavHostController) {
    composable<LoginRoute> {
        LoginScreen(
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