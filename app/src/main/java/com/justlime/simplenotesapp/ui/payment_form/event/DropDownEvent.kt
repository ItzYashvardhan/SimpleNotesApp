package com.justlime.simplenotesapp.ui.payment_form.event

sealed interface DropDownEvent {
    data class OnOptionChange(val selectedOption: String) : DropDownEvent
    data class OnExpandedChange(val isExpanded: Boolean): DropDownEvent
    object OnDismissRequest: DropDownEvent
}