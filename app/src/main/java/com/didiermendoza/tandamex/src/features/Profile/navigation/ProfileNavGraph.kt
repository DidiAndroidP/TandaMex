package com.didiermendoza.tandamex.src.features.Profile.navigate

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.didiermendoza.tandamex.src.core.navigation.ProfileRoute
import com.didiermendoza.tandamex.src.core.navigation.LoginRoute
import com.didiermendoza.tandamex.src.features.Profile.presentation.screens.ProfileScreen

fun NavGraphBuilder.profileNavGraph(navController: NavHostController) {
    composable<ProfileRoute> {
        ProfileScreen(
            onLogoutClick = {
                navController.navigate(LoginRoute) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}