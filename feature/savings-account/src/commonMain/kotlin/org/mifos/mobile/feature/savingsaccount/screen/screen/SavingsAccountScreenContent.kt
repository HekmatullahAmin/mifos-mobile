/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.screen.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_account_active
import mifos_mobile.feature.savings_account.generated.resources.feature_account_approved
import mifos_mobile.feature.savings_account.generated.resources.feature_account_closed
import mifos_mobile.feature.savings_account.generated.resources.feature_account_matured
import mifos_mobile.feature.savings_account.generated.resources.feature_account_string_and_string
import mifos_mobile.feature.savings_account.generated.resources.feature_account_submitted
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.feature.savingsaccount.screen.component.AccountCard

@Composable
fun SavingsAccountScreenContent(
    accountList: List<SavingAccount>,
    onAccountSelected: (String, Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
    ) {
        items(items = accountList, key = { account -> account.id }) { savingAccount ->
            SavingsAccountListItem(
                savingAccount = savingAccount,
                onAccountSelected = onAccountSelected,
            )
        }
    }
}

@Composable
private fun SavingsAccountListItem(
    savingAccount: SavingAccount,
    onAccountSelected: (accountType: String, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (indicatorColor, statusDescription, balanceTextColor) = getAccountStatusDisplayAttributes(
        savingAccount = savingAccount,
    )

    val currencySymbolOrCode =
        savingAccount.currency?.displaySymbol ?: savingAccount.currency?.code ?: ""

    val formattedBalance = CurrencyFormatter.format(
        balance = savingAccount.accountBalance,
        currencyCode = savingAccount.currency?.code,
        maximumFractionDigits = 2,
    )

    val amountAndCurrency = stringResource(
        Res.string.feature_account_string_and_string,
        formattedBalance,
        currencySymbolOrCode,
    )

    AccountCard(
        accountNo = savingAccount.accountNo,
        productName = savingAccount.productName,
        statusString = statusDescription,
        balance = amountAndCurrency,
        indicatorColor = indicatorColor,
        textColor = balanceTextColor,
        onClick = {
            onAccountSelected(Constants.SAVINGS_ACCOUNTS, savingAccount.id)
        },
        modifier = modifier,
    )
}

@Composable
private fun getAccountStatusDisplayAttributes(savingAccount: SavingAccount): Triple<Color, String, Color?> {
    return when {
        savingAccount.status?.active == true -> {
            Triple(
                MaterialTheme.colorScheme.primary,
                stringResource(Res.string.feature_account_active) +
                    savingAccount.lastActiveTransactionDate?.let { DateHelper.getDateAsString(it) },
                MaterialTheme.colorScheme.primary,
            )
        }

        savingAccount.status?.approved == true -> {
            Triple(
                MaterialTheme.colorScheme.secondaryContainer,
                stringResource(Res.string.feature_account_approved) +
                    (
                        savingAccount.timeLine?.approvedOnDate?.let { DateHelper.getDateAsString(it) }
                            ?: ""
                        ),
                null,
            )
        }

        savingAccount.status?.submittedAndPendingApproval == true -> {
            Triple(
                MaterialTheme.colorScheme.tertiaryContainer,
                stringResource(Res.string.feature_account_submitted) +
                    (
                        savingAccount.timeLine?.submittedOnDate?.let {
                            DateHelper.getDateAsString(
                                it,
                            )
                        }
                        ),
                null,
            )
        }

        savingAccount.status?.matured == true -> {
            Triple(
                MaterialTheme.colorScheme.errorContainer,
                stringResource(Res.string.feature_account_matured) +
                    savingAccount.lastActiveTransactionDate?.let { DateHelper.getDateAsString(it) },
                MaterialTheme.colorScheme.errorContainer,
            )
        }

        else -> {
            Triple(
                MaterialTheme.colorScheme.surfaceVariant,
                stringResource(Res.string.feature_account_closed) +
                    (
                        savingAccount.timeLine?.closedOnDate?.let { DateHelper.getDateAsString(it) }
                            ?: ""
                        ),
                null,
            )
        }
    }
}
