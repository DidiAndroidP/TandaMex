package com.didiermendoza.tandamex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.didiermendoza.tandamex.src.core.appContext.AppContainerHolder
import com.didiermendoza.tandamex.src.core.navigation.NavigationWrapper
import com.didiermendoza.tandamex.src.core.navigation.LoginRoute
import com.didiermendoza.tandamex.src.core.ui.theme.TandaMexTheme
import com.didiermendoza.tandamex.src.features.Register.navigation.RegisterNavGraph
import com.didiermendoza.tandamex.src.features.Login.navigation.LoginNavGraph
import com.didiermendoza.tandamex.src.features.Home.navigate.HomeNavGraph
import com.didiermendoza.tandamex.src.features.Profile.navigate.ProfileNavGraph
import com.didiermendoza.tandamex.src.features.Tanda.navigation.TandaNavGraph

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        AppContainerHolder.init(applicationContext)
        val container = AppContainerHolder.get()

        val navGraphs = listOf(
            LoginNavGraph(container.loginModule),
            RegisterNavGraph(container.registerModule),
            HomeNavGraph(container.homeModule),
            ProfileNavGraph(container.profileModule),
            TandaNavGraph(container.tandaModule)
        )

        val startDestination = LoginRoute

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