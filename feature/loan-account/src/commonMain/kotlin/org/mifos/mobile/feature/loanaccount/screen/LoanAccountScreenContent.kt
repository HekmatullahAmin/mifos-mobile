/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.screen

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
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_account_approved
import mifos_mobile.feature.loan_account.generated.resources.feature_account_closed
import mifos_mobile.feature.loan_account.generated.resources.feature_account_disbursement
import mifos_mobile.feature.loan_account.generated.resources.feature_account_submitted
import mifos_mobile.feature.loan_account.generated.resources.feature_account_withdrawn
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.feature.loanaccount.component.AccountCard

@Composable
fun LoanAccountScreenContent(
    accountList: List<LoanAccount>,
    onAccountSelected: (String, Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
    ) {
        items(items = accountList, key = { account -> account.id }) { loanAccount ->
            LoanAccountListItem(
                loanAccount = loanAccount,
                onAccountSelected = onAccountSelected,
            )
        }
    }
}

@Composable
private fun LoanAccountListItem(
    loanAccount: LoanAccount,
    onAccountSelected: (accountType: String, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (indicatorColor, statusDescription, balanceTextColor) = getAccountStatusDisplayAttributes(
        loanAccount = loanAccount,
    )

    val formattedBalance = CurrencyFormatter.format(
        balance = loanAccount.loanBalance,
        currencyCode = loanAccount.currency?.code,
        maximumFractionDigits = 2,
    )

    AccountCard(
        accountNo = loanAccount.accountNo,
        productName = loanAccount.productName,
        statusString = statusDescription,
        balance = formattedBalance,
        indicatorColor = indicatorColor,
        textColor = balanceTextColor,
        onClick = {
            onAccountSelected(Constants.LOAN_ACCOUNTS, loanAccount.id)
        },
        modifier = modifier,
    )
}

@Composable
private fun getAccountStatusDisplayAttributes(loanAccount: LoanAccount): Triple<Color, String, Color?> {
    return when {
        loanAccount.status?.active == true && loanAccount.inArrears == true -> {
            Triple(
                MaterialTheme.colorScheme.error,
                stringResource(resource = Res.string.feature_account_disbursement) +
                    loanAccount.timeline?.actualDisbursementDate?.let {
                        DateHelper.getDateAsString(it)
                    },
                MaterialTheme.colorScheme.error,
            )
        }

        loanAccount.status?.active == true -> {
            Triple(
                MaterialTheme.colorScheme.primary,
                stringResource(resource = Res.string.feature_account_disbursement) +
                    loanAccount.timeline?.actualDisbursementDate?.let {
                        DateHelper.getDateAsString(it)
                    },
                MaterialTheme.colorScheme.primary,
            )
        }

        loanAccount.status?.waitingForDisbursal == true -> {
            Triple(
                MaterialTheme.colorScheme.secondary,
                stringResource(resource = Res.string.feature_account_approved) +
                    loanAccount.timeline?.approvedOnDate?.let { DateHelper.getDateAsString(it) },
                null,
            )
        }

        loanAccount.status?.pendingApproval == true -> {
            Triple(
                MaterialTheme.colorScheme.tertiary,
                stringResource(resource = Res.string.feature_account_submitted) +
                    loanAccount.timeline?.submittedOnDate?.let { DateHelper.getDateAsString(it) },
                null,
            )
        }

        loanAccount.status?.overpaid == true -> {
            Triple(
                MaterialTheme.colorScheme.tertiaryContainer,
                stringResource(resource = Res.string.feature_account_approved) +
                    loanAccount.timeline?.actualDisbursementDate?.let {
                        DateHelper.getDateAsString(it)
                    },
                MaterialTheme.colorScheme.tertiaryContainer,
            )
        }

        loanAccount.status?.closed == true -> {
            Triple(
                MaterialTheme.colorScheme.onSurface,
                stringResource(resource = Res.string.feature_account_closed) +
                    loanAccount.timeline?.closedOnDate?.let { DateHelper.getDateAsString(it) },
                null,
            )
        }

        else -> {
            Triple(
                MaterialTheme.colorScheme.outline,
                stringResource(resource = Res.string.feature_account_withdrawn) +
                    loanAccount.timeline?.withdrawnOnDate?.let { DateHelper.getDateAsString(it) },
                null,
            )
        }
    }
}
