package com.didiermendoza.tandamex.src.features.Register.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.didiermendoza.tandamex.src.core.navigation.LoginRoute
import com.didiermendoza.tandamex.src.core.navigation.RegisterRoute
import com.didiermendoza.tandamex.src.features.Register.presentation.screens.RegisterScreen

fun NavGraphBuilder.registerNavGraph(navController: NavHostController) {
    composable<RegisterRoute> {
        RegisterScreen(
            onNavigateToLogin = {
                navController.navigate(LoginRoute) {
                    popUpTo<RegisterRoute> { inclusive = true }
                }
            }
        )
    }
}