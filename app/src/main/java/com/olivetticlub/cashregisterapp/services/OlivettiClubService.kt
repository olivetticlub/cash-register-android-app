package com.olivetticlub.cashregisterapp.services

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class OlivettiClubService(private val baseUrl: String) : CouponGeneration {

    override fun retrieveCoupon(merchant: String, completion: (Deal?) -> Unit) {
        val retrofit = Retrofit.Builder()
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()

        retrofit.create(OlivettiClubServiceApi::class.java).consume(ConsumeCouponRequest(merchant))
            .enqueue(object :
                Callback<ConsumeCouponResponse> {
                override fun onFailure(call: Call<ConsumeCouponResponse>, t: Throwable) {
                    completion.invoke(null)
                }

                override fun onResponse(
                    call: Call<ConsumeCouponResponse>,
                    response: Response<ConsumeCouponResponse>
                ) {
                    if (response.code() == 200) {
                        response.body()?.let {
                            completion.invoke(it.deal)
                            return
                        }
                    }

                    completion.invoke(null)
                }

            })
    }
}

interface OlivettiClubServiceApi {

    @POST("coupons/consume")
    fun consume(@Body request: ConsumeCouponRequest): Call<ConsumeCouponResponse>
}

data class ConsumeCouponResponse(var deal: Deal)

data class ConsumeCouponRequest(var merchant: String)
