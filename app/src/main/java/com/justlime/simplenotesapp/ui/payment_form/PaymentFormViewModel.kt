package com.justlime.simplenotesapp.ui.payment_form

import androidx.lifecycle.ViewModel
import com.justlime.simplenotesapp.ui.payment_form.enums.PaymentMode
import com.justlime.simplenotesapp.ui.payment_form.event.DropDownEvent
import com.justlime.simplenotesapp.ui.payment_form.event.PaymentEvent
import com.justlime.simplenotesapp.ui.payment_form.state.DropDownState
import com.justlime.simplenotesapp.ui.payment_form.state.PaymentState
import com.justlime.simplenotesapp.utils.currentTimeZone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class PaymentFormViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentState(
        partyDropDownState = DropDownState(PaymentMode.entries.map { it.label }, PaymentMode.CASH.label),
        paymentModeDropDownState = DropDownState(PaymentMode.entries.map { it.label}, PaymentMode.CASH.label)
    ))
    val uiState = _uiState.asStateFlow()


    fun onEvent(event: PaymentEvent) {
        when (event) {
            is PaymentEvent.OnCreditAmountChange -> {
                _uiState.value = _uiState.value.copy(
                    creditAmount = event.creditAmount.toFloatOrNull() ?: _uiState.value.creditAmount
                )
            }

            is PaymentEvent.OnAdvanceAmountChange -> {
                _uiState.value = _uiState.value.copy(
                    advanceAmount = event.advanceAmount
                )
            }

            is PaymentEvent.OnBillNumberChanged -> {
                _uiState.value = _uiState.value.copy(
                    billNumber = event.number
                )
            }

            is PaymentEvent.OnDateChange -> {
                _uiState.value = _uiState.value.copy(
                    billDate = event.date.toLocalDateTime().toInstant(currentTimeZone) //TODO
                )
            }

            PaymentEvent.OnAddItemClick -> {


            }


            PaymentEvent.OnNewItemClick -> {

            }

            PaymentEvent.OnNewPartyClick -> {

            }

            is PaymentEvent.OnPaymentModeChange -> {

            }

            PaymentEvent.OnSubmitClick -> {

            }

            is PaymentEvent.OnPartyAction -> {
                when (val action = event.action) {
                    DropDownEvent.OnDismissRequest -> {
                        _uiState.value = _uiState.value.copy(
                            partyDropDownState = _uiState.value.partyDropDownState.copy(
                                isExpanded = false
                            )
                        )
                    }

                    is DropDownEvent.OnExpandedChange -> {
                        _uiState.value = _uiState.value.copy(
                            partyDropDownState = _uiState.value.partyDropDownState.copy(
                                isExpanded = action.isExpanded
                            )
                        )

                    }

                    is DropDownEvent.OnOptionChange -> {
                        _uiState.value = _uiState.value.copy(
                            partyDropDownState = _uiState.value.partyDropDownState.copy(
                                selectedOption = action.selectedOption
                            )
                        )
                    }
                }
            }

            is PaymentEvent.OnPaymentModeAction -> {
                when (val action = event.action) {
                    DropDownEvent.OnDismissRequest -> {
                        _uiState.value = _uiState.value.copy(
                            paymentModeDropDownState = _uiState.value.partyDropDownState.copy(
                                isExpanded = false
                            )
                        )
                    }

                    is DropDownEvent.OnExpandedChange -> {
                        _uiState.value = _uiState.value.copy(
                            paymentModeDropDownState = _uiState.value.paymentModeDropDownState.copy(
                                isExpanded = action.isExpanded
                            )
                        )

                    }

                    is DropDownEvent.OnOptionChange -> {
                        _uiState.value = _uiState.value.copy(
                            paymentModeDropDownState = _uiState.value.partyDropDownState.copy(
                                selectedOption = action.selectedOption
                            )
                        )
                    }
                }
            }

        }
    }
}