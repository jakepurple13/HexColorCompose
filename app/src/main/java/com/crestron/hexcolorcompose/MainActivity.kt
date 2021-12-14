package com.crestron.hexcolorcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crestron.hexcolorcompose.networking.ColorName
import com.crestron.hexcolorcompose.networking.ColorResponse
import com.crestron.hexcolorcompose.ui.theme.HexColorComposeTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HexColorComposeTheme { HexColor() } }
    }
}

@ExperimentalFoundationApi
@Composable
fun HexColor(viewModel: ColorViewModel = viewModel()) {
    /**
    * this will animate the background color
    * we use `by` so that we don't need to call`state.value`
    **/
    val backgroundColor by animateColorAsState(
        targetValue = if(viewModel.hexValue.length == 6) Color("#${viewModel.hexValue}".toColorInt()) else Color.Black
    )
    /**
     * this animates the font color
     **/
    val fontColor by animateColorAsState(if (backgroundColor.luminance() > .5f) Color.Black else Color.White)

    /**
     * This comes from Google's Accompanist library. This allows us to change the color of the system bars
     */
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(backgroundColor)

    /**
     * this is the info we get back from thecolorapi.com. More logic is in the viewModel method
     */
    val colorName by viewModel.colorApi(viewModel.hexValue)
        .subscribeAsState(initial = ColorResponse(ColorName("--", "")))

    /**
     * Scaffolds are some of the most useful layouts, giving support for topbar, bottombar, floating action button, drawer, bottomsheet, and backdrop.
     * For our purposes, we don't need anything but a topbar.
     */
    Scaffold(
        topBar = {
            /**
             * Normally we would use a TopAppBar here and use the slot-based layout design,
             * but, our purposes are different. We could do without a scaffold, but I believe its good to explore what Compose has.
             */
            Header(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                fontColor = fontColor,
                hexValue = viewModel.hexValue,
                /**
                 * Because we have the backgroundColor as an animateColorAsState, these values will animate to a new value
                 */
                rgbValue = "(${(backgroundColor.red * 100).toInt()},${(backgroundColor.green * 100).toInt()},${(backgroundColor.blue * 100).toInt()})",
                nameValue = colorName.name.value
            )
        },
        backgroundColor = backgroundColor
    ) { p ->

        /**
         * The keypad
         */
        val digits = arrayOf("D", "E", "F", "A", "B", "C", "7", "8", "9", "4", "5", "6", "1", "2", "3")

        /**
         * In the Raywenderlich version, EVERYTHING was manually created in xml and laid out and constrained.
         * While Compose does have a ConstraintLayout library, we don't need it since the LazyVerticalGrid does exactly what we need it to do.
         *
         * ### Note: The Compose ConstraintLayout library works VERY similarly to the xml version except that in Compose, the constraints are done in the modifier.
         */
        LazyVerticalGrid(
            cells = GridCells.Fixed(3),
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize(),
            contentPadding = p
        ) {

            /**
             * In all `Lazy` versions, [LazyColumn], [LazyRow], [LazyVerticalGrid], we have the `item` or `items` methods.
             * These are like the `RecyclerView.Adapter`, allowing you to put a new view in each one
             */
            items(digits) { DigitItem(digit = it, fontColor = fontColor, onPress = viewModel::addDigit) }
            item {
                DigitItem(
                    digit = "⊗",
                    fontColor = fontColor,
                    onPress = { viewModel.clear() }
                )
            }
            item { DigitItem(digit = "0", fontColor = fontColor, onPress = viewModel::addDigit) }
            item {
                DigitItem(
                    digit = "⌫",
                    fontColor = fontColor,
                    onPress = { viewModel.removeDigit() }
                )
            }
        }
    }
}

/**
 * This allows you to preview a Composable/View before loading it onto a device
 */
@ExperimentalFoundationApi
@Preview
@Composable
fun HexColorPreview() {
    HexColorComposeTheme { HexColor() }
}