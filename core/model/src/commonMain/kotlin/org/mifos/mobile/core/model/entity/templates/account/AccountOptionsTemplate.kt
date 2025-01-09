/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.account

import org.mifos.mobile.core.model.MyParcelize
import org.mifos.mobile.core.model.Parcelable

/**
 * Created by Rajan Maurya on 10/03/17.
 */

@MyParcelize
data class AccountOptionsTemplate(

    var fromAccountOptions: List<AccountOption> = ArrayList(),

    var toAccountOptions: List<AccountOption> = ArrayList(),

    var fromAccountTypeOptions: List<AccountType> = ArrayList(),

    var toAccountTypeOptions: List<AccountType> = ArrayList(),
) : Parcelable
