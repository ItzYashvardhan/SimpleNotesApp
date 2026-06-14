package com.justlime.simplenotesapp.ui.payment_form.state

data class DropDownState(
    var options: List<String> = emptyList(),
    var selectedOption: String = "",
    var isExpanded: Boolean = false,
)