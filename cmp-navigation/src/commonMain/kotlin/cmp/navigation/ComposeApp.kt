/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cmp.navigation.navigation.NavGraphRoute.AUTH_GRAPH
import cmp.navigation.navigation.NavGraphRoute.PASSCODE_GRAPH
import cmp.navigation.navigation.RootNavGraph
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.feature.accounts.navigation.accountsScreenRoute
import org.mifos.mobile.feature.accounts.navigation.navigateToAccountsScreen

@Composable
fun ComposeApp(
    modifier: Modifier = Modifier,
    networkMonitor: NetworkMonitor = koinInject(),
    viewModel: ComposeAppViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    val navDestination = when (uiState) {
        is MainUiState.Loading -> AUTH_GRAPH
        is MainUiState.Success -> if ((uiState as MainUiState.Success).userData.isAuthenticated) {
            PASSCODE_GRAPH
        } else {
            AUTH_GRAPH
        }

        else -> AUTH_GRAPH
    }

    MifosMobileTheme {
//        NavHost(
//            navController = navController,
//            startDestination = "Start",
//        ) {
//            composable(route = "Start") {
//                navController.navigateToAccountsScreen()
//            }
//            accountsScreenRoute(
//                navigateBack = navController::popBackStack,
//                navigateToAccountDetail = { _, _ -> },
//                navigateToLoanApplicationScreen = { },
//                navigateToSavingsApplicationScreen = { },
//            )
//        }
        RootNavGraph(
            modifier = modifier.fillMaxSize(),
            networkMonitor = networkMonitor,
            navHostController = navController,
            startDestination = navDestination,
//            onClickLogout = {
//                viewModel.logOut()
//                navController.navigate(AUTH_GRAPH) {
//                    popUpTo(navController.graph.id) {
//                        inclusive = true
//                    }
//                }
//            },
        )
    }
}
