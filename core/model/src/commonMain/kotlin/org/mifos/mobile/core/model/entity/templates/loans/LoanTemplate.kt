/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.loans

import kotlinx.serialization.SerialName
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.AmortizationType
import org.mifos.mobile.core.model.entity.accounts.loan.DaysInMonthType
import org.mifos.mobile.core.model.entity.accounts.loan.DaysInYearType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestCalculationPeriodType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestRateFrequencyType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestType
import org.mifos.mobile.core.model.entity.accounts.loan.RepaymentFrequencyType
import org.mifos.mobile.core.model.entity.accounts.loan.TermPeriodFrequencyType

/**
 * Created by Rajan Maurya on 15/07/16.
 */

@Parcelize
data class LoanTemplate(

    val clientId: Int? = null,

    val clientAccountNo: String? = null,

    val clientName: String? = null,

    val clientOfficeId: Int? = null,

    val loanProductName: String? = null,

    @SerialName("isLoanProductLinkedToFloatingRate")
    val loanProductLinkedToFloatingRate: Boolean? = null,

    val fundId: Int? = null,

    val fundName: String? = null,

    val currency: Currency? = null,

    val principal: Double? = null,

    val approvedPrincipal: Double? = null,

    val proposedPrincipal: Double? = null,

    val termFrequency: Int? = null,

    val termPeriodFrequencyType: TermPeriodFrequencyType? = null,

    val numberOfRepayments: Int? = null,

    val repaymentEvery: Int? = null,

    val repaymentFrequencyType: RepaymentFrequencyType? = null,

    val interestRatePerPeriod: Double? = null,

    val interestRateFrequencyType: InterestRateFrequencyType? = null,

    val annualInterestRate: Double? = null,

    @SerialName("isFloatingInterestRate")
    val floatingInterestRate: Boolean? = null,

    val amortizationType: AmortizationType? = null,

    val interestType: InterestType? = null,

    val interestCalculationPeriodType: InterestCalculationPeriodType? = null,

    val allowPartialPeriodInterestCalcualtion: Boolean? = null,

    val transactionProcessingStrategyId: Int? = null,

    val graceOnArrearsAgeing: Int? = null,

    val timeline: Timeline? = null,

    val productOptions: List<ProductOptions> = ArrayList(),

    val loanOfficerOptions: List<LoanOfficerOptions> = ArrayList(),

    val loanPurposeOptions: List<LoanPurposeOptions> = ArrayList(),

    val fundOptions: List<FundOptions> = ArrayList(),

    val termFrequencyTypeOptions: List<TermFrequencyTypeOptions> = ArrayList(),

    val repaymentFrequencyTypeOptions: List<RepaymentFrequencyTypeOptions> = ArrayList(),

    val repaymentFrequencyNthDayTypeOptions: List<RepaymentFrequencyNthDayTypeOptions> = ArrayList(),

    val repaymentFrequencyDaysOfWeekTypeOptions: List<RepaymentFrequencyDaysOfWeekTypeOptions> = ArrayList(),

    val interestRateFrequencyTypeOptions: List<InterestRateFrequencyTypeOptions> = ArrayList(),

    val amortizationTypeOptions: List<AmortizationTypeOptions> = ArrayList(),

    val interestTypeOptions: List<InterestTypeOptions> = ArrayList(),

    val interestCalculationPeriodTypeOptions: List<InterestCalculationPeriodType> = ArrayList(),

    val transactionProcessingStrategyOptions: List<TransactionProcessingStrategyOptions> = ArrayList(),

    val chargeOptions: List<ChargeOptions> = ArrayList(),

    val loanCollateralOptions: List<LoanCollateralOptions> = ArrayList(),

    val multiDisburseLoan: Boolean? = null,

    val canDefineInstallmentAmount: Boolean? = null,

    val canDisburse: Boolean? = null,

    val product: Product? = null,

    val daysInMonthType: DaysInMonthType? = null,

    val daysInYearType: DaysInYearType? = null,

    @SerialName("isInterestRecalculationEnabled")
    val interestRecalculationEnabled: Boolean? = null,

    @SerialName("isvaliableInstallmentsAllowed")
    val valiableInstallmentsAllowed: Boolean? = null,

    val minimumGap: Int? = null,

    val maximumGap: Int? = null,

    val accountLinkingOptions: List<AccountLinkingOptions> = ArrayList(),

) : Parcelable {
    /**
     * Required to set default value to the Fund spinner
     *
     * @param fundId The value received from the Template for that particular loanProduct
     * @return Returns the index of the fundOption list where the specified fundId is located
     */
    fun getFundNameFromId(fundId: Int): Int {
        for (i in fundOptions.indices) {
            if (fundOptions[i].id == fundId) {
                return i
            }
        }
        return 0
    }
}
