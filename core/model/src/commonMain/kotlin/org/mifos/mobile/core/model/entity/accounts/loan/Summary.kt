/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.loan

import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Parcelize
data class Summary(
    val principalDisbursed: Double = 0.toDouble(),

    val principalPaid: Double = 0.toDouble(),

    val interestCharged: Double = 0.toDouble(),

    val interestPaid: Double = 0.toDouble(),

    val feeChargesCharged: Double = 0.toDouble(),

    val penaltyChargesCharged: Double = 0.toDouble(),

    val penaltyChargesWaived: Double = 0.toDouble(),

    val totalExpectedRepayment: Double = 0.toDouble(),

    val interestWaived: Double = 0.toDouble(),

    val totalRepayment: Double = 0.toDouble(),

    val feeChargesWaived: Double = 0.toDouble(),

    val totalOutstanding: Double = 0.toDouble(),

    private val overdueSinceDate: List<Int>? = null,

    val currency: Currency? = null,
) : Parcelable {

    fun getOverdueSinceDate(): List<Int>? {
        return if (overdueSinceDate == null) {
            null
        } else {
            overdueSinceDate
        }
    }
}
