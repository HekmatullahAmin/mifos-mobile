/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_account_empty_savings_accounts
import mifos_mobile.feature.savings_account.generated.resources.feature_account_error_black
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.model.entity.accounts.savings.Currency
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.savings.Status
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.feature.savingsaccount.utils.AccountState
import org.mifos.mobile.feature.savingsaccount.viewmodel.SavingsAccountViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsAccountScreen(
    checkboxOptionsLabels: List<StringResource?>,
    searchQuery: String,
//    isSearchActive: Boolean,
//    isFiltered: Boolean,
    onAccountSelected: (accountType: AccountType, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavingsAccountViewmodel = koinViewModel(),
) {
    val uiState = viewModel.accountUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullToRefreshState()

    LaunchedEffect(checkboxOptionsLabels, searchQuery) {
        viewModel.loadSavingsAccounts(
            searchQuery = searchQuery,
//            isFiltered = isFiltered,
//            isSearchActive = isSearchActive,
            selectedCheckboxLabels = checkboxOptionsLabels
        )
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        state = pullRefreshState,
//        onRefresh = viewModel::refresh,
        onRefresh = {
            viewModel.refresh(
                searchQuery = searchQuery,
//                isFiltered = isFiltered,
//                isSearchActive = isSearchActive,
                selectedCheckboxLabels = checkboxOptionsLabels
            )
        },
        modifier = modifier,
    ) {
        when (val state = uiState.value) {
            is AccountState.Error -> {
                MifosErrorComponent(
                    isNetworkConnected = isNetworkAvailable,
                    isRetryEnabled = true,
//                    onRetry = viewModel::loadSavingsAccounts,
                    onRetry = {
                        viewModel.loadSavingsAccounts(
                            searchQuery = searchQuery,
//                            isFiltered = isFiltered,
//                            isSearchActive = isSearchActive,
                            selectedCheckboxLabels = checkboxOptionsLabels
                        )
                    },
                )
            }

            is AccountState.Loading -> {
                MifosProgressIndicatorOverlay()
            }

            is AccountState.Empty -> {
                EmptyDataView(
                    icon = vectorResource(resource = Res.drawable.feature_account_error_black),
                    error = Res.string.feature_account_empty_savings_accounts,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is AccountState.Success -> {
                val savingsAccounts = state.filteredSavingsAccounts

                SavingsAccountScreenContent(
                    accountList = savingsAccounts,
                    onAccountSelected = onAccountSelected,
                )
            }
        }
    }
}
