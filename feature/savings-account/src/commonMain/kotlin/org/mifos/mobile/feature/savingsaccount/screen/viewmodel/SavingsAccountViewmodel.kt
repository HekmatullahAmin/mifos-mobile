/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.savingsaccount.screen.model.CheckboxStatus
import org.mifos.mobile.feature.savingsaccount.screen.utils.AccountState
import org.mifos.mobile.feature.savingsaccount.screen.utils.FilterUtil
import org.mifos.mobile.feature.savingsaccount.screen.utils.StatusUtil
import org.mifos.mobile.feature.savingsaccount.screen.utils.getSavingsAccountFilterLabels

class SavingsAccountViewmodel(
    private val accountsRepositoryImpl: AccountsRepository,
    networkMonitor: NetworkMonitor,
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val clientId = requireNotNull(userPreferencesRepository.clientId.value)

    val isNetworkAvailable = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    @Suppress("PropertyName")
    private val _accountsUiState = MutableStateFlow<AccountState>(AccountState.Loading)
    val accountUiState: StateFlow<AccountState> = _accountsUiState.asStateFlow()

    private fun filterAccountsBySearchQuery(
        accounts: List<SavingAccount?>?,
        searchQuery: String?,
    ): List<SavingAccount> {
        val searchTerm = searchQuery?.lowercase().orEmpty()

        return accounts.orEmpty().filter { account ->
            account?.run {
                listOf(productName, accountNo)
                    .any { it?.lowercase()?.contains(searchTerm) == true }
            } ?: false
        }.filterNotNull()
    }

    //    private fun getFilterSavingsAccountList(
    private fun filterAccountsByStatus(
        accountsList: List<SavingAccount?>,
        filterList: List<CheckboxStatus>,
    ): List<SavingAccount> {
        val savingsAccountFilterCriteria = getSavingsAccountFilterLabels()

        return filterList
            .filter { it.isChecked }
            .flatMap { selectedCheckboxStatus ->
                getAccountsMatchingStatus(
                    accounts = accountsList,
                    status = selectedCheckboxStatus,
                    statusCriteria = savingsAccountFilterCriteria,
                )
            }
            .distinct()
    }

    private fun getAccountsMatchingStatus(
        accounts: List<SavingAccount?>?,
        status: CheckboxStatus?,
        statusCriteria: FilterUtil,
    ): List<SavingAccount> {
        return accounts.orEmpty().filter { account ->
            when {
                status?.status == statusCriteria.activeString && account?.status?.active == true -> true
                status?.status == statusCriteria.approvedString && account?.status?.approved == true -> true
                status?.status == statusCriteria.approvalPendingString &&
                    account?.status?.submittedAndPendingApproval == true -> true

                status?.status == statusCriteria.maturedString && account?.status?.matured == true -> true
                status?.status == statusCriteria.closedString && account?.status?.closed == true -> true
                else -> false
            }
        }.filterNotNull()
    }

    fun getUpdatedFilteredAccountList(
        searchQuery: String,
        isFiltered: Boolean,
        isSearchActive: Boolean,
        accountList: List<SavingAccount>,
    ): List<SavingAccount> {
        return when {
            isFiltered && isSearchActive -> {
                val updatedFilterList = filterAccountsByStatus(
                    accountsList = accountList,
                    filterList = StatusUtil.getSavingsAccountStatusList(),
                )

                filterAccountsBySearchQuery(
                    accounts = updatedFilterList,
                    searchQuery = searchQuery,
                )
            }

            isSearchActive -> {
                filterAccountsBySearchQuery(
                    accounts = accountList,
                    searchQuery = searchQuery,
                )
            }

            isFiltered -> {
                filterAccountsByStatus(
                    accountsList = accountList,
                    filterList = StatusUtil.getSavingsAccountStatusList(),
                )
            }

            else -> {
                accountList
            }
        }
    }

    fun loadSavingsAccounts() {
        viewModelScope.launch {
            _accountsUiState.value = AccountState.Loading
            accountsRepositoryImpl.loadAccounts(
                clientId = clientId,
                accountType = AccountType.SAVINGS.name,
            ).catch {
                _accountsUiState.value = AccountState.Error
            }.collect { clientAccounts ->
                _accountsUiState.value =
                    AccountState.Success(clientAccounts.data?.savingsAccounts)
            }
        }
    }
}
