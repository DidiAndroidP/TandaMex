package com.didiermendoza.tandamex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
// Imports Core
import com.didiermendoza.tandamex.src.core.di.AppContainer
import com.didiermendoza.tandamex.src.core.navigation.NavigationWrapper
import com.didiermendoza.tandamex.src.core.navigation.LoginRoute
import com.didiermendoza.tandamex.src.core.navigation.HomeRoute
import com.didiermendoza.tandamex.src.core.ui.theme.TandaMexTheme

// Imports Features
import com.didiermendoza.tandamex.src.features.Register.di.RegisterModule
import com.didiermendoza.tandamex.src.features.Register.navigation.RegisterNavGraph
import com.didiermendoza.tandamex.src.features.Login.di.LoginModule
import com.didiermendoza.tandamex.src.features.Login.navigation.LoginNavGraph
import com.didiermendoza.tandamex.src.features.Home.di.HomeModule
import com.didiermendoza.tandamex.src.features.Home.navigate.HomeNavGraph
import com.didiermendoza.tandamex.src.features.Profile.di.ProfileModule
import com.didiermendoza.tandamex.src.features.Profile.navigate.ProfileNavGraph
import com.didiermendoza.tandamex.src.features.Tanda.di.TandaModule
import com.didiermendoza.tandamex.src.features.Tanda.navigation.TandaNavGraph

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        appContainer = AppContainer(applicationContext)

        val registerModule = RegisterModule(appContainer)
        val loginModule = LoginModule(appContainer)
        val homeModule = HomeModule(appContainer)
        val profileModule = ProfileModule(appContainer)
        val tandaModule = TandaModule(appContainer)

        val navGraphs = listOf(
            LoginNavGraph(loginModule),
            RegisterNavGraph(registerModule),
            HomeNavGraph(homeModule),
            ProfileNavGraph(profileModule),
            TandaNavGraph(tandaModule)
        )

        val startDestination = if (appContainer.tokenManager.getToken() != null) {
            HomeRoute
        } else {
            LoginRoute
        }

        setContent {
            TandaMexTheme {
                NavigationWrapper(
                    navGraphs = navGraphs,
                    startDestination = startDestination
                )
            }
        }
    }
}