package com.didiermendoza.tandamex.src.features.Register.navigation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.didiermendoza.tandamex.src.core.navigation.FeatureNavGraph
import com.didiermendoza.tandamex.src.core.navigation.Routes
import com.didiermendoza.tandamex.src.features.Register.di.RegisterModule
import com.didiermendoza.tandamex.src.features.Register.presentation.screens.RegisterScreen
import com.didiermendoza.tandamex.src.features.Register.presentation.viewmodels.RegisterViewModel

class RegisterNavGraph(
    private val registerModule: RegisterModule
) : FeatureNavGraph {

    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.composable(route = "register_route") {

            val viewModel: RegisterViewModel = viewModel(
                factory = registerModule.provideRegisterViewModelFactory()
            )

            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.navigate("login_route") {
                        popUpTo("register_route") { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate("home_route") {
                        popUpTo("register_route") { inclusive = true }
                    }
                }
            )
        }
    }
}