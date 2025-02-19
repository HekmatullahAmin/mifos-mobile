/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.savingsaccount.utils.AccountState
import org.mifos.mobile.feature.savingsaccount.utils.FilterUtil

/**
 * ViewModel responsible for managing savings accounts and their states.
 *
 * This ViewModel interacts with repositories to fetch, filter, and manage savings accounts.
 * It also monitors network connectivity and manages UI state updates.
 *
 * @property accountsRepositoryImpl Repository responsible for fetching savings accounts.
 * @property networkMonitor Monitors the network status.
 * @property userPreferencesRepository Stores user-related preferences, including client ID.
 */
class SavingsAccountViewmodel(
    private val accountsRepositoryImpl: AccountsRepository,
    networkMonitor: NetworkMonitor,
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    /** Client ID retrieved from user preferences. */
    private val clientId = requireNotNull(userPreferencesRepository.clientId.value)

    /**
     * Tracks whether a refresh operation is in progress.
     *
     * Used by [PullToRefreshBox] to indicate whether the list is currently refreshing.
     */
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    /** Tracks network availability using [NetworkMonitor]. */
    val isNetworkAvailable = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /** Holds the current state of savings accounts UI. */
    @Suppress("PropertyName")
    private val _accountsUiState = MutableStateFlow<AccountState>(AccountState.Loading)
    val accountUiState: StateFlow<AccountState> = _accountsUiState.asStateFlow()

    /**
     * Filters savings accounts based on the search query.
     *
     * @param accounts List of savings accounts.
     * @param searchQuery Search query string.
     * @return Filtered list of savings accounts that match the search query.
     */
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

    /**
     * Filters savings accounts based on selected filter labels using [FilterUtil].
     *
     * @param accounts List of savings accounts.
     * @param selectedCheckboxLabels List of selected filter labels.
     * @return List of savings accounts that match the selected filters.
     */
    private fun filterAccountsByStatus(
        accounts: List<SavingAccount>,
        selectedCheckboxLabels: List<StringResource?>,
    ): List<SavingAccount> {
        return selectedCheckboxLabels
            .mapNotNull { label -> FilterUtil.fromLabel(label) }
            .flatMap { filterUtil ->
                accounts.filter(filterUtil.matchCondition)
            }
            .distinct()
    }

    /**
     * Retrieves savings accounts based on search query and selected filters.
     *
     * @param searchQuery The search term entered by the user.
     * @param isFiltered Whether filtering by status is enabled.
     * @param isSearchActive Whether the search query is active.
     * @param selectedCheckboxLabels List of selected filter labels.
     * @param accounts The list of all savings accounts.
     * @return A filtered list of savings accounts based on the applied filters.
     */
    fun getFilteredAccounts(
        searchQuery: String,
        isFiltered: Boolean,
        isSearchActive: Boolean,
        selectedCheckboxLabels: List<StringResource?>,
        accounts: List<SavingAccount>,
    ): List<SavingAccount> {
        val filteredByStatus = if (isFiltered) filterAccountsByStatus(accounts, selectedCheckboxLabels) else accounts
        return if (isSearchActive) filterAccountsBySearchQuery(filteredByStatus, searchQuery) else filteredByStatus
    }

    /**
     * Triggers a refresh operation when the user pulls down to refresh.
     *
     * This function is called by [PullToRefreshBox] to reload savings accounts.
     */
    fun refresh() {
        _isRefreshing.value = true
        loadSavingsAccounts()
    }

    /**
     * Loads savings accounts for the client and updates the UI state.
     *
     * This function fetches savings accounts from the repository and updates the UI accordingly.
     * If an error occurs during fetching, it updates the UI state to [AccountState.Error].
     * Once accounts are successfully loaded, [_isRefreshing] is reset to false.
     */
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
                _isRefreshing.value = false
            }
        }
    }
}
