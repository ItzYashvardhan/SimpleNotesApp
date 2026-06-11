package com.justlime.simplenotesapp.ui.payment_form

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justlime.simplenotesapp.ui.theme.smokeColor


@Composable
fun PaymentForm(innerPadding: PaddingValues) {

    Column(
        Modifier
            .padding(innerPadding + PaddingValues(16.dp, 0.dp))
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        PayContainer()
        TermsAndConditionContainer()
        ItemButtonContainer()
    }

}

@Composable
fun PayContainer() {
    Column(
        Modifier
            .decorativeBorder()
            .fillMaxWidth()
    ) {
        var creditAmount by rememberSaveable { mutableIntStateOf(0) }
        var advanceAmount by rememberSaveable { mutableIntStateOf(0) }

        Row {
            Label("Party Name ", true)
        }
        Spacer(Modifier.height(6.dp))
        AmountComponent("Credit Amount", creditAmount.toString()) {
            creditAmount = it.toInt()
        }
        Spacer(Modifier.height(6.dp))
        AmountComponent("Advance Amount", advanceAmount.toString()) {
            advanceAmount = it.toInt()
        }
    }
}

@Composable
fun Label(label: String, isRequired: Boolean = false) {
    val labelSize = 12.sp
    Text(label, fontSize = labelSize, fontWeight = FontWeight.Bold)
    if (isRequired) {
        Text("*", fontSize = labelSize, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ItemButtonContainer() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        ItemButton(Icons.Outlined.Add, "Add Item")
        Spacer(Modifier.width(16.dp))
        ItemButton(Icons.Outlined.Add, "New Item")
    }
}

@Composable
fun ItemButton(icon: ImageVector, label: String, onClick: () -> Unit = {}) {

    Row(
        modifier = Modifier
            .decorativeBorder(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, "", tint = Color.Blue)
        Spacer(Modifier.width(4.dp))
        Text(label, color = Color.Blue)
    }
}


@Composable
fun TermsAndConditionContainer() {
    Card(Modifier.fillMaxWidth()) {
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
        BasicTextField(
            value,
            onValueChange,
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
                    if (value.isEmpty()) {
                        Text(text = "Enter text", color = Color.LightGray)
                    }
                    innerTextField()
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)

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
    val strokeWidth = 1.dp
    val dashLength = 4.dp
    val gapLength = 6.dp
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
        .padding(12.dp)
}

@Composable
fun Dp.toPx(): Float = with(LocalDensity.current) { this@toPx.toPx() }
