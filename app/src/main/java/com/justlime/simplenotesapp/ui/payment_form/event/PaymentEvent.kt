package com.justlime.simplenotesapp.ui.payment_form.event

sealed interface PaymentEvent {
    //amount
    data class OnCreditAmountChange(val creditAmount: String): PaymentEvent
    data class OnAdvanceAmountChange(val advanceAmount: String): PaymentEvent

    //others
    data class OnBillNumberChanged(val number: Number): PaymentEvent
    data class OnDateChange(val date: String): PaymentEvent
    data class OnPaymentModeChange(val paymentMode: String): PaymentEvent

    //dropdowns
    data class OnPartyAction(val action: DropDownEvent): PaymentEvent
    data class OnPaymentModeAction(val action: DropDownEvent): PaymentEvent
    //clicks
    object OnNewPartyClick : PaymentEvent
    object OnAddItemClick: PaymentEvent
    object OnNewItemClick: PaymentEvent
    object OnSubmitClick: PaymentEvent
}