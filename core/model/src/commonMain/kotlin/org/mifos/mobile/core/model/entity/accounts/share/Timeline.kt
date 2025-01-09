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
data class Timeline(
    @Expose
    var submittedOnDate: List<Int>? = null,

    @Expose
    var submittedByUsername: String? = null,

    @Expose
    var submittedByFirstname: String? = null,

    @Expose
    var submittedByLastname: String? = null,

    @Expose
    var approvedDate: List<Int>? = null,

    @Expose
    var approvedByUsername: String? = null,

    @Expose
    var approvedByFirstname: String? = null,

    @Expose
    var approvedByLastname: String? = null,

    @Expose
    var activatedDate: List<Int>? = null,

    @Expose
    var activatedByUsername: String? = null,

    @Expose
    var activatedByFirstname: String? = null,

    @Expose
    var activatedByLastname: String? = null,

) : Parcelable
