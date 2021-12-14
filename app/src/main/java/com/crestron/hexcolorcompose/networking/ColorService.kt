package com.crestron.hexcolorcompose.networking

import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class ColorResponse(val name: ColorName)
data class ColorName(val value: String, val closest_named_hex: String)

object ColorApi {
    const val API = "https://www.thecolorapi.com/"
    private val colorService = ColorService.create()

    /**
     * We check to see if [hexString] is 6 digits so we assume its valid. If its not 6 digits, we return a default response
     */
    fun getClosestColor(hexString: String): Observable<ColorResponse> = if(hexString.length == 6)
        colorService.getColor(hexString)
    else
        Observable.just(ColorResponse(ColorName("--", "")))
}

/**
 * This is all retrofit
 */
interface ColorService {
    companion object {
        fun create(): ColorService {

            val client = OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(ColorApi.API)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ColorService::class.java)
        }
    }

    @GET("id")
    fun getColor(@Query("hex") hex: String): Observable<ColorResponse>
}