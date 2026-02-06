package com.didiermendoza.tandamex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
// Imports de tu arquitectura
import com.didiermendoza.tandamex.src.core.di.AppContainer
import com.didiermendoza.tandamex.src.core.navigation.NavigationWrapper
import com.didiermendoza.tandamex.src.core.ui.theme.TandaMexTheme
// Imports de Features (Registro)
import com.didiermendoza.tandamex.src.features.Register.di.RegisterModule
import com.didiermendoza.tandamex.src.features.Register.navigation.RegisterNavGraph

class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        appContainer = AppContainer(applicationContext)

        val registerModule = RegisterModule(appContainer)

        val navGraphs = listOf(
            RegisterNavGraph(registerModule)
        )
        setContent {
            TandaMexTheme {
                NavigationWrapper(navGraphs = navGraphs)
            }
        }
    }
}