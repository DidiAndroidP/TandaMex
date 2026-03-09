package com.didiermendoza.tandamex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.didiermendoza.tandamex.src.core.navigation.NavigationWrapper
import com.didiermendoza.tandamex.src.core.ui.theme.TandaMexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TandaMexTheme {
                NavigationWrapper()
            }
        }
    }
}