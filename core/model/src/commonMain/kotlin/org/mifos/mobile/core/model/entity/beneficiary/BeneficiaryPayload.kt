/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.beneficiary

import org.mifos.mobile.core.model.MyParcelize
import org.mifos.mobile.core.model.Parcelable

@MyParcelize
data class BeneficiaryPayload(
    internal var locale: String = "en_GB",

    var name: String? = null,

    var accountNumber: String? = null,

    var accountType: Int? = 0,

    var transferLimit: Float? = 0f,

    var officeName: String? = null,
) : Parcelable
