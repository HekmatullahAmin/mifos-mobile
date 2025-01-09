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

import org.mifos.mobile.core.model.MyParcelize
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.entity.accounts.Account

@MyParcelize
data class LoanAccount(
    var loanProductId: Long = 0,

    var externalId: String? = null,

    var numberOfRepayments: Long = 0,

    var accountNo: String? = null,

    var productName: String? = null,

    var productId: Int? = null,

    var loanProductName: String? = null,

    var clientName: String? = null,

    var loanProductDescription: String? = null,

    var principal: Double = 0.toDouble(),

    var annualInterestRate: Double = 0.toDouble(),

    var status: Status? = null,

    var loanType: LoanType? = null,

    var loanCycle: Int? = null,

    var loanBalance: Double = 0.toDouble(),

    var amountPaid: Double = 0.toDouble(),

    var currency: Currency?,

    var inArrears: Boolean? = null,

    var summary: Summary? = null,

    var loanPurposeName: String? = null,

    var timeline: Timeline?,

) : Account(), Parcelable
