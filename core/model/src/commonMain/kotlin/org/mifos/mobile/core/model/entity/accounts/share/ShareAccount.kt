/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.share

import org.mifos.mobile.core.model.Expose
import org.mifos.mobile.core.model.MyParcelize
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.entity.accounts.Account
import org.mifos.mobile.core.model.entity.accounts.savings.Currency

@MyParcelize
data class ShareAccount(

    @Expose
    var accountNo: String? = null,

    @Expose
    var totalApprovedShares: Int? = null,

    @Expose
    var totalPendingForApprovalShares: Int? = null,

    @Expose
    var productId: Int? = null,

    @Expose
    var productName: String? = null,

    @Expose
    var shortProductName: String? = null,

    @Expose
    var status: Status? = null,

    @Expose
    var currency: Currency? = null,

    @Expose
    var timeline: Timeline? = null,

) : Account(), Parcelable
