package com.justlime.simplenotesapp.ui.payment_form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justlime.simplenotesapp.ui.payment_form.enums.PaymentTableHeader
import com.justlime.simplenotesapp.ui.payment_form.event.DropDownEvent
import com.justlime.simplenotesapp.ui.payment_form.event.PaymentEvent
import com.justlime.simplenotesapp.ui.payment_form.state.PaymentState
import com.justlime.simplenotesapp.ui.theme.LightPurpleGrey100
import com.justlime.simplenotesapp.ui.theme.LightPurpleGrey100Border
import com.justlime.simplenotesapp.ui.theme.LightPurpleGrey80
import com.justlime.simplenotesapp.ui.theme.Purple40
import com.justlime.simplenotesapp.ui.theme.smokeColor
import com.justlime.simplenotesapp.utils.DecimalFormatter
import com.justlime.simplenotesapp.utils.DecimalInputVisualTransformation
import com.justlime.simplenotesapp.utils.currentTimeZone
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant


@Composable
fun PaymentFormRoute(
    innerPadding: PaddingValues,
    viewModel: PaymentFormViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val event: (PaymentEvent) -> Unit = viewModel::onEvent
    val tableHorizontalScrollState = rememberScrollState()
    Column(modifier = Modifier.padding(innerPadding)) {
        PaymentFormScreen(state, event, tableHorizontalScrollState)
    }
}

// @formatter:off
@Composable
fun PaymentFormScreen(
    state: PaymentState,
    onEvent: (event: PaymentEvent) -> Unit = {},
    tableHorizontalScrollState: ScrollState
) {

    LazyColumn(
        Modifier
            .padding(PaddingValues(16.dp, 8.dp)),
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PayContainer(
                    isExpanded = state.partyDropDownState.isExpanded,
                    selectedOption = state.partyDropDownState.selectedOption,
                    parties = state.partyDropDownState.options,
                    creditAmount = state.creditAmount,
                    advanceAmount = state.advanceAmount,
                    onCreditValueChange = { onEvent(PaymentEvent.OnCreditAmountChange(it)) },
                    onAmountValueChange = { onEvent(PaymentEvent.OnAdvanceAmountChange(it)) },
                    onDropDownValueChange = {onEvent( PaymentEvent.OnPartyAction(DropDownEvent.OnOptionChange(it)))},
                    onExpandedChange = {onEvent( PaymentEvent.OnPartyAction(DropDownEvent.OnExpandedChange(it)))},
                    onDismissRequest = {onEvent( PaymentEvent.OnPartyAction(DropDownEvent.OnDismissRequest))}
                )
                BillContainer(
                    billNumber = state.billNumber,
                    date = state.billDate,
                    onBillNumberChange = { onEvent(PaymentEvent.OnBillNumberChanged(it)) },
                    onDateChange = { onEvent(PaymentEvent.OnDateChange(it)) }
                )
                TableContainer(
                    headers = PaymentTableHeader.entries.map { it.name },
                    data = state.tableData,
                    scrollState = tableHorizontalScrollState
                )
                TableItemButtonContainer(
                    onAddClick = { onEvent(PaymentEvent.OnAddItemClick) },
                    onNewClick = { onEvent(PaymentEvent.OnNewItemClick) }
                )
                TermsAndConditionContainer()
                PaymentModeContainer(
                    isExpanded = state.paymentModeDropDownState.isExpanded,
                    selectedOption = state.paymentModeDropDownState.selectedOption,
                    options = state.paymentModeDropDownState.options,
                    onValueChange = {onEvent( PaymentEvent.OnPaymentModeAction(DropDownEvent.OnOptionChange(it)))},
                    onExpandedChange = {onEvent( PaymentEvent.OnPaymentModeAction(DropDownEvent.OnExpandedChange(it)))},
                    onDismissRequest = {onEvent( PaymentEvent.OnPaymentModeAction(DropDownEvent.OnDismissRequest))}
                )
                TotalContainer()
            }
        }
    }

}
//@formatter:on
@Composable
fun PayContainer(
    isExpanded: Boolean,
    selectedOption: String,
    parties: List<String>,
    creditAmount: Float = 0.0F,
    advanceAmount: String = "",
    onCreditValueChange: (String) -> Unit = {},
    onAmountValueChange: (String) -> Unit = {},
    onDropDownValueChange: (String) -> Unit = {},
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Column(
        Modifier
            .decorativeBorder()
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Label("Party Name ", true)
            Spacer(Modifier.width(6.dp))
            DropdownMenu(
                isExpanded, selectedOption, parties,
                onValueChange = onDropDownValueChange,
                onDismissRequest = onDismissRequest,
                onExpandedChange = onExpandedChange
            )
            Spacer(Modifier.width(4.dp))
            Button(
                onClick = {},
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Add New +", fontSize = 10.sp)
            }
        }
        Spacer(Modifier.height(6.dp))
        AmountComponent("Credit Amount", creditAmount.toString()) {
            onCreditValueChange(it)
        }
        Spacer(Modifier.height(12.dp))
        AmountComponent(label = "Advance Amount", advanceAmount) {
            onAmountValueChange(it)
        }
    }
}

@Composable
fun AmountComponent(label: String, text: String, onValueChange: (String) -> Unit) {
    Text(text)
    Column {
        Label(label)
        Spacer(Modifier.height(6.dp))
        BasicTextField(
            // Note: See the warning below about modifying text directly in the value parameter
            value = if (text == "0.0") "" else if (text.toIntOrNull().toString() == text) text.toIntOrNull().toString() else text,
            onValueChange = { input ->
                // 1. Instantly swap any typed comma into a standard period
                val normalizedInput = input.replace(',', '.')

                // 2. Allow digits and at most ONE period (changed [,] to \\.?)
                if (normalizedInput.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    // Pass the clean, valid decimal string up to your state
                    onValueChange(normalizedInput)
                }
            },
            modifier = Modifier
                .width(250.dp)
                .height(34.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
        )
    }
}

@Composable
fun BillContainer(
    billNumber: Number,
    date: Instant,
    onBillNumberChange: (Number) -> Unit,
    onDateChange: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    Column(
        modifier = Modifier
            .decorativeBorder()
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Bill No.", true)
            Spacer(Modifier.width(12.dp))
            TextComponent("{Bill No", billNumber, onBillNumberChange)
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Bill Date", true)
            Spacer(Modifier.width(12.dp))
            DateComponent(date, datePickerState, onDateChange)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {

        }
    }

}

@Composable
fun TextComponent(label: String, value: Number, onValueChange: (Number) -> Unit) {

    val finalValue = value.toString()

    Column {
        BasicTextField(
            finalValue.ifEmpty { "0" },
            onValueChange = {
                if (it.isDigitsOnly()) onValueChange(it.toInt()) else onValueChange(value)
            },
            modifier = Modifier
                .width(150.dp)
                .height(34.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (finalValue.isEmpty() || finalValue == "0") {
                        Text(label, color = Color.Gray)
                    } else {
                        innerTextField()
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}


@Composable
fun TableContainer(
    headers: List<String>,
    data: List<List<String>>,
    scrollState: ScrollState
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        TableHeader(scrollState, headers)
        TableContent(scrollState, data)
        TableFooter(scrollState, headers.size)
    }
}

@Composable
fun TableHeader(
    scrollState: ScrollState,
    headers: List<String>
) {
    val stroke = BorderStroke(1.dp, LightPurpleGrey100Border)
    val roundedCorner = 16.dp
    val shape = RoundedCornerShape(roundedCorner, roundedCorner)
    Row(
        modifier = Modifier
            .border(stroke, shape = shape)
            .clip(shape)
            .background(Purple40)
            .horizontalScroll(scrollState)
            .padding(8.dp)
    ) {
        headers.forEach {
            Text(
                it,
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .width(110.dp)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun TableContent(scrollState: ScrollState, data: List<List<String>>) {
    val dataSize = data.size
//    val offSet = 10
    repeat(data.size) { index ->
        val cell = data[index]
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            cell.forEach { cell ->
                Text(
                    text = cell,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun TableFooter(scrollState: ScrollState, cellAmount: Int) {
    val stroke = BorderStroke(1.dp, LightPurpleGrey100Border)
    val roundedCorner = 16.dp
    val shape = RoundedCornerShape(0.dp, 0.dp, roundedCorner, roundedCorner)
    Row(
        modifier = Modifier
            .border(stroke, shape = shape)
            .clip(shape)
            .background(LightPurpleGrey80)
            .horizontalScroll(scrollState)
            .padding(8.dp)
    ) {
        repeat(cellAmount) {
            Text(
                "",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .width(110.dp)
                    .padding(4.dp)
            )
        }
    }
}


@Composable
fun DateComponent(
    value: Instant,
    datePickerState: DatePickerState,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value.toLocalDateTime(currentTimeZone).toString(),
        onValueChange = {},
        modifier = Modifier
            .width(150.dp)
            .height(34.dp),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.toString().isEmpty() || value.toString() == "") {
                    Row {
                        Icon(Icons.Default.CalendarMonth, "", tint = Color.Gray)
                        CurrentDateDisplay()
                    }
                } else {
                    innerTextField()
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )

}

@Composable
fun CurrentDateDisplay() {
    val dateTime = Clock.System.now().toLocalDateTime(currentTimeZone)
    val customFormat = remember {
        LocalDateTime.Format {
            day()
            char('/')
            monthNumber()
            char('/')
            year()
        }
    }
    Text(text = remember(dateTime) { dateTime.format(customFormat) }, fontSize = 16.sp)
}

@Composable
fun PaymentModeContainer(
    isExpanded: Boolean,
    selectedOption: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onExpandedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.decorativeBorder()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Label("Payment Mode ", true)
            Spacer(Modifier.width(8.dp))
            DropdownMenu(
                isExpanded, selectedOption, options, 200.dp,
                onValueChange = onValueChange,
                onDismissRequest = onDismissRequest,
                onExpandedChange = onExpandedChange
            )
        }
    }
}

@Composable
fun TotalContainer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .decorativeBorder()
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Label("Total Amount")
            Spacer(Modifier.weight(1f))
            LockedField("0")
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth()) {
            Text("Additional Amount")
            Modifier.weight(1f)
            Text("+")
        }
        Spacer(Modifier.height(4.dp))
        HorizontalDivider()
        Spacer(Modifier.height(4.dp))
        Row(Modifier.fillMaxWidth()) {
            Label("Net Amount")
            Spacer(Modifier.weight(1f))
            LockedField("0")
        }
    }
}

@Composable
fun LockedField(value: String, width: Dp = 120.dp) {
    Column(
        modifier = Modifier
            .background(LightPurpleGrey80)
            .width(width)
            .padding(16.dp, 4.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(value)
    }

}

@Composable
fun Label(label: String, isRequired: Boolean = false) {
    val labelSize = 12.sp
    Text(label, fontSize = labelSize, fontWeight = FontWeight.Bold)
    if (isRequired) {
        Text("*", fontSize = labelSize, fontWeight = FontWeight.Bold, color = Color.Red)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    expanded: Boolean,
    selectedOption: String,
    options: List<String>,
    width: Dp = 110.dp,
    onValueChange: (String) -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onExpandedChange: (Boolean) -> Unit
) {
    ExposedDropdownMenuBox(expanded, onExpandedChange) {
        BasicTextField(
            value = selectedOption,
            onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(fontSize = 12.sp),
            modifier = Modifier
                .width(width)
                .height(30.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .menuAnchor()
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row {
                        innerTextField()
                    }
                    Row {
                        Spacer(Modifier.weight(1f))
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    }
                }

            },
            singleLine = true,
            readOnly = true,
        )
        ExposedDropdownMenu(expanded, onDismissRequest) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onValueChange(it)
                        onDismissRequest()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }

    }

}


@Composable
fun TableItemButtonContainer(
    onAddClick: () -> Unit,
    onNewClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        ItemButton(Icons.Outlined.Add, "+ Add Item", onAddClick)
        Spacer(Modifier.weight(1f))
        ItemButton(Icons.Outlined.Add, "+ New Item", onNewClick)
    }
}

@Composable
fun ItemButton(icon: ImageVector, label: String, onClick: () -> Unit = {}) {

    Row(
        modifier = Modifier
            .decorativeBorder()
            .padding(24.dp, 0.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Blue, fontSize = 14.sp)
    }
}


@Composable
fun TermsAndConditionContainer() {
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(containerColor = LightPurpleGrey100),
        border = BorderStroke(2.dp, LightPurpleGrey100Border)
    ) {
        val fontSize = 9.sp
        Column(Modifier.padding(16.dp)) {
            Label("Term and Conditions")
            Text("1. Goods once sold will no be taken back.", fontSize = fontSize)
            Text(
                "2. Interest @ 18% pa will be charged if the payment for Business is not made within the stipulated time.",
                fontSize = fontSize
            )
            Text("3. INR 300 will be charged in case of cheque return.", fontSize = fontSize)
        }
    }
}


@Preview("Payment Form Preview", showBackground = true)
@Composable
fun PaymentFormPreview() {
    PaymentFormRoute(PaddingValues(10.dp))
}


@Composable
fun Modifier.decorativeBorder(): Modifier {
    val strokeWidth = 3.dp
    val dashLength = 3.dp
    val gapLength = 4.dp
    val cap = StrokeCap.Round

    val shape: Shape = RoundedCornerShape(6.dp)
    val stroke = Stroke(
        cap = cap, width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx())
        )
    )


    return this
        .drawWithContent {
            val outline = shape.createOutline(size, layoutDirection, this)
            drawContent()
            drawOutline(
                outline = outline,
                style = stroke,
                color = smokeColor
            )
        }
        .padding(8.dp)
}

@Composable
fun Dp.toPx(): Float = with(LocalDensity.current) { this@toPx.toPx() }
