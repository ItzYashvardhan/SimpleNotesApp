package com.justlime.simplenotesapp.ui.payment_form.state

import kotlin.time.Clock
import kotlin.time.Instant

data class PaymentState(
    //amount
    var creditAmount: Float = 0.0f,
    var advanceAmount: String = "",
    var additionalAmount: Float = 0.0f,


    //others
    var billNumber: Number = 0,
    var billDate: Instant = Clock.System.now(),
    var tableData: List<List<String>> = emptyList(),

    //dropdown
    var partyDropDownState: DropDownState = DropDownState(),
    var paymentModeDropDownState: DropDownState = DropDownState(),
) {
    val totalAmount: Double get() = (creditAmount.toDouble() + (advanceAmount.toDoubleOrNull()?:0.toDouble()))
    val netAmount: Double get() = totalAmount + additionalAmount
}