package com.crestron.hexcolorcompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.crestron.hexcolorcompose.networking.ColorApi
import com.crestron.hexcolorcompose.networking.ColorResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Here is our ViewModel!
 */
class ColorViewModel : ViewModel() {
    /**
     * Be aware of the fact that this is a [mutableStateOf]. Also notice the `by`.
     * Just like animateColorAsState, we use `by` so we can get/set without the need to call `state.value`.
     *
     * This allows any Composable that references [hexValue] to update when [hexValue] updates.
     */
    var hexValue by mutableStateOf("")

    /**
     * We add a digit to the end of [hexValue]
     */
    fun addDigit(d: String) {
        if (hexValue.length < 6) hexValue += d
    }

    /**
     * We remove a digit from the end of [hexValue]
     */
    fun removeDigit() {
        if (hexValue.isNotEmpty()) hexValue = hexValue.dropLast(1)
    }

    /**
     * We clear [hexValue]
     */
    fun clear() {
        hexValue = ""
    }

    /**
     * We get info from thecolorapi.com
     */
    fun colorApi(hex: String): Observable<ColorResponse> = ColorApi.getClosestColor(hex)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}