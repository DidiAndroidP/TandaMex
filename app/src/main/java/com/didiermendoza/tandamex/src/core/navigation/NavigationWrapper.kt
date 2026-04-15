package com.didiermendoza.tandamex.src.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.didiermendoza.tandamex.src.features.Login.navigation.loginNavGraph
import com.didiermendoza.tandamex.src.features.Register.navigation.registerNavGraph
import com.didiermendoza.tandamex.src.features.Home.navigate.homeNavGraph
import com.didiermendoza.tandamex.src.features.Profile.navigate.profileNavGraph
import com.didiermendoza.tandamex.src.features.Tanda.navigation.tandaNavGraph


@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        enterTransition = {
            slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(400))
        },
        exitTransition = {
            slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(400))
        },
        popEnterTransition = {
            slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(400))
        }
    ) {
        loginNavGraph(navController)
        registerNavGraph(navController)
        homeNavGraph(navController)
        profileNavGraph(navController)
        tandaNavGraph(navController)
    }
}