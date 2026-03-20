package com.didiermendoza.tandamex.src.features.wallet.navigate

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.didiermendoza.tandamex.src.core.navigation.WalletRoute
import com.didiermendoza.tandamex.src.features.wallet.presentation.page.WalletScreen

fun NavGraphBuilder.walletNavGraph(navController: NavHostController){
    composable<WalletRoute> {
        WalletScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}