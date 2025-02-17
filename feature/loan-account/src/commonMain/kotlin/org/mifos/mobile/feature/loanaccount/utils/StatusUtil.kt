/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.utils

import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_account_active
import mifos_mobile.feature.loan_account.generated.resources.feature_account_approval_pending
import mifos_mobile.feature.loan_account.generated.resources.feature_account_approved
import mifos_mobile.feature.loan_account.generated.resources.feature_account_closed
import mifos_mobile.feature.loan_account.generated.resources.feature_account_matured
import org.mifos.mobile.feature.loanaccount.model.CheckboxStatus

object StatusUtil {
    internal fun getLoanAccountStatusList(): List<CheckboxStatus> {
        val arrayList = arrayListOf<CheckboxStatus>()

        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_active,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approved,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approval_pending,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_matured,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_closed,
            ),
        )
        return arrayList
    }
}
