/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.viewmodel

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
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.loanaccount.model.CheckboxStatus
import org.mifos.mobile.feature.loanaccount.utils.AccountState
import org.mifos.mobile.feature.loanaccount.utils.FilterUtil
import org.mifos.mobile.feature.loanaccount.utils.StatusUtil
import org.mifos.mobile.feature.loanaccount.utils.getAccountsFilterLabels

class LoanAccountViewmodel(
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
        accounts: List<LoanAccount?>?,
        searchQuery: String?,
    ): List<LoanAccount> {
        val searchTerm = searchQuery?.lowercase().orEmpty()

        return accounts.orEmpty().filter { account ->
            account?.run {
                listOf(productName, accountNo)
                    .any { it?.lowercase()?.contains(searchTerm) == true }
            } ?: false
        }.filterNotNull()
    }

    private fun filterAccountsByStatus(
        accountsList: List<LoanAccount?>,
        filterList: List<CheckboxStatus>,
    ): List<LoanAccount> {
        val savingsAccountFilterCriteria = getAccountsFilterLabels()

        return filterList
            .filter { it.isChecked }
            .flatMap { selectedCheckboxStatus ->
                getAccountsMatchingStatus(
                    accounts = accountsList,
                    checkboxStatus = selectedCheckboxStatus,
                    statusCriteria = savingsAccountFilterCriteria,
                )
            }
            .distinct()
    }

    private fun getAccountsMatchingStatus(
        accounts: List<LoanAccount?>?,
        checkboxStatus: CheckboxStatus?,
        statusCriteria: FilterUtil,
    ): List<LoanAccount> {
//        TODO: rename it and refactor code
        return accounts.orEmpty().filter { account ->
            when (checkboxStatus?.status) {
                statusCriteria.inArrearsString -> account?.inArrears == true
                statusCriteria.activeString -> account?.status?.active == true
                statusCriteria.waitingForDisburseString -> account?.status?.waitingForDisbursal == true
                statusCriteria.approvalPendingString -> account?.status?.pendingApproval == true
                statusCriteria.overpaidString -> account?.status?.overpaid == true
                statusCriteria.closedString -> account?.status?.closed == true
                statusCriteria.withdrawnString -> account?.status?.isLoanTypeWithdrawn() == true
                else -> false
            }
        }.filterNotNull()
    }

    fun getUpdatedFilteredAccountList(
        searchQuery: String,
        isFiltered: Boolean,
        isSearchActive: Boolean,
        accountList: List<LoanAccount>,
    ): List<LoanAccount> {
        return when {
            isFiltered && isSearchActive -> {
                val updatedFilterList = filterAccountsByStatus(
                    accountsList = accountList,
                    filterList = StatusUtil.getLoanAccountStatusList(),
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
                    filterList = StatusUtil.getLoanAccountStatusList(),
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
                accountType = AccountType.LOAN.name,
            ).catch {
                _accountsUiState.value = AccountState.Error
            }.collect { clientAccounts ->
                _accountsUiState.value =
                    AccountState.Success(clientAccounts.data?.loanAccounts)
            }
        }
    }
}
