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

@MyParcelize
data class Status(
    @Expose
    var id: Int? = null,
    @Expose
    var code: String? = null,

    @Expose
    var value: String? = null,

    @Expose
    var submittedAndPendingApproval: Boolean? = null,

    @Expose
    var approved: Boolean? = null,

    @Expose
    var rejected: Boolean? = null,

    @Expose
    var active: Boolean? = null,

    @Expose
    var closed: Boolean? = null,

) : Parcelable
