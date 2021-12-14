package com.crestron.hexcolorcompose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

/**
 * This is the digit that is seen on the screen.
 * As you can see, its only a [Text] with some Modifiers.
 *
 * It is always good practice to include a [modifier] in custom Composables to make them generic.
 */
@ExperimentalFoundationApi
@Composable
fun DigitItem(modifier: Modifier = Modifier, digit: String, fontColor: Color, onPress: (String) -> Unit) {
    Text(
        digit,
        color = fontColor,
        fontSize = 45.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            /**
             * There is a reason for this. Usually you can use a [Surface] or a [Button], even a [TextButton], but these are only [Text].
             * So we change [rememberRipple] to be unbounded so the ripple can go outside of its bounds.
             */
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
            ) { onPress(digit) }
            .then(modifier)
    )
}

/**
 * Here is the Header! Very simple.
 * Columns go vertically and Rows go horizontally.
 */
@Composable
fun Header(modifier: Modifier = Modifier, fontColor: Color, hexValue: String, rgbValue: String, nameValue: String) {
    Column(modifier = modifier) {
        Text(
            "#$hexValue",
            color = fontColor,
            fontSize = 45.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            Text(
                rgbValue,
                color = fontColor,
                fontSize = 28.sp
            )
            Text(
                nameValue,
                color = fontColor,
                fontSize = 28.sp
            )
        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun DigitPreview() {
    DigitItem(digit = "1", fontColor = Color.Black, onPress = {})
}

@Preview
@Composable
fun HeaderPreview() {
    Header(fontColor = Color.White, hexValue = "123456", rgbValue = "(1,2,3)", nameValue = "Black")
}