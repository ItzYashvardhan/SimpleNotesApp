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
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.justlime.simplenotesapp.ui.theme.LightPurpleGrey100
import com.justlime.simplenotesapp.ui.theme.LightPurpleGrey100Border
import com.justlime.simplenotesapp.ui.theme.LightPurpleGrey80
import com.justlime.simplenotesapp.ui.theme.Purple40
import com.justlime.simplenotesapp.ui.theme.smokeColor
import com.justlime.simplenotesapp.utils.currentTimeZone
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


@Composable
fun PaymentForm(innerPadding: PaddingValues) {

    LazyColumn(
        Modifier
            .padding(innerPadding + PaddingValues(16.dp, 8.dp)),
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PayContainer()
                BillContainer()
                TableContainer()
                ItemButtonContainer()
                TermsAndConditionContainer()
                PaymentModeContainer()
                TotalContainer()
            }
        }
    }

}

@Composable
fun TableContainer() {
    val scrollState = rememberScrollState()
    val headers = listOf(
        "#",
        "Product Name",
        "Purchase Price",
        "Bag Type",
        "Bag/Pcs Qty"
    )
    val data = emptyList<List<String>>()

    Column(modifier = Modifier.fillMaxWidth()) {
        TableHeader(scrollState, headers)
//        TableContent(scrollState, emptyList())
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
    LazyColumn {
        items(data) { row ->
            Row(modifier = Modifier.horizontalScroll(scrollState)) {
                row.forEach { cell ->
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
fun PayContainer() {
    Column(
        Modifier
            .decorativeBorder()
            .fillMaxWidth()
    ) {
        var creditAmount by rememberSaveable { mutableFloatStateOf(0.0f) }
        var advanceAmount by rememberSaveable { mutableFloatStateOf(0.0f) }

        var isExpanded by rememberSaveable() { mutableStateOf(false) }
        var selectedOption by rememberSaveable { mutableStateOf("Cash") }
        val options = remember { mutableListOf("Cash", "UPI", "Card", "Net Banking") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Label("Party Name ", true)
            Spacer(Modifier.width(6.dp))
            DropdownMenu(
                isExpanded, selectedOption, options,
                onValueChange = { selectedOption = it },
                onDismissRequest = { isExpanded = false },
                onExpandedChange = { isExpanded = it }
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
            it.toFloatOrNull()?.apply {
                creditAmount = this
            }
        }
        Spacer(Modifier.height(12.dp))
        AmountComponent("Advance Amount", advanceAmount.toString()) {
            it.toFloatOrNull()?.apply {
                advanceAmount = this
            }
        }
    }
}

@Composable
fun BillContainer() {
    var billNumber by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    val datePickerState = rememberDatePickerState()
    Column(
        modifier = Modifier
            .decorativeBorder()
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Bill No.", true)
            Spacer(Modifier.width(12.dp))
            TextComponent("Bill No", billNumber) {
                billNumber = it
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Bill Date", true)
            Spacer(Modifier.width(12.dp))
            DateComponent(date, datePickerState) {
                date = it
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {

        }
    }

}

@Composable
fun TextComponent(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        BasicTextField(
            value,
            onValueChange = {
                if (it.isDigitsOnly()) {
                    onValueChange(it)
                } else {
                    onValueChange(value)
                }
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
                    if (value.isEmpty() || value == "0") {
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
fun DateComponent(
    value: String,
    datePickerState: DatePickerState,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value,
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
                if (value.isEmpty() || value == "") {
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
fun PaymentModeContainer() {
    Column(
        modifier = Modifier.decorativeBorder()
    ) {
        var isExpanded by rememberSaveable() { mutableStateOf(false) }
        var selectedOption by rememberSaveable { mutableStateOf("Cash") }
        val options = remember { mutableListOf("Cash", "UPI", "Card", "Net Banking") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Label("Payment Mode ", true)
            Spacer(Modifier.width(8.dp))
            DropdownMenu(
                isExpanded, selectedOption, options, 200.dp,
                onValueChange = { selectedOption = it },
                onDismissRequest = { isExpanded = false },
                onExpandedChange = { isExpanded = it }
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
fun ItemButtonContainer() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        ItemButton(Icons.Outlined.Add, "+ Add Item")
        Spacer(Modifier.weight(1f))
        ItemButton(Icons.Outlined.Add, "+ New Item")
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

@Composable
fun AmountComponent(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Label(label)
        Spacer(Modifier.height(6.dp))
        BasicTextField(
            value,
            onValueChange = {
                if (it.isNotEmpty() && it.toDoubleOrNull() != null) {
                    onValueChange(it)
                    return@BasicTextField
                }
                onValueChange(value)
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
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)

        )
    }
}


@Preview("Payment Form Preview", showBackground = true)
@Composable
fun PaymentFormPreview() {
    PaymentForm(PaddingValues(10.dp))
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
