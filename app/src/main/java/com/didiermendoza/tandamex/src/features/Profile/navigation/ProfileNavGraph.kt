package com.didiermendoza.tandamex.src.features.Profile.navigate

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.didiermendoza.tandamex.src.core.navigation.FeatureNavGraph
import com.didiermendoza.tandamex.src.core.navigation.ProfileRoute
import com.didiermendoza.tandamex.src.core.navigation.LoginRoute
import com.didiermendoza.tandamex.src.features.Profile.di.ProfileModule
import com.didiermendoza.tandamex.src.features.Profile.presentation.screens.ProfileScreen
import com.didiermendoza.tandamex.src.features.Profile.presentation.viewmodels.ProfileViewModel

class ProfileNavGraph(
    private val profileModule: ProfileModule
) : FeatureNavGraph {

    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {

        navGraphBuilder.composable<ProfileRoute> {
            val viewModel: ProfileViewModel = viewModel(
                factory = profileModule.provideProfileViewModelFactory()
            )

            ProfileScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}