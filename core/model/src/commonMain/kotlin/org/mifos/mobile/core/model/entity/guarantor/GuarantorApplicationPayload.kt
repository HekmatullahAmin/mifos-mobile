/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.guarantor

import kotlinx.serialization.SerialName

/*
 * Created by saksham on 23/July/2018
 */

data class GuarantorApplicationPayload(

    @SerialName("guarantorTypeId")
    var guarantorTypeId: Long?,

    @SerialName("firstname")
    var firstName: String?,

    @SerialName("lastname")
    var lastName: String?,

    @SerialName("city")
    var city: String? = "",
)
