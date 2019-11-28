package com.olivetticlub.cashregisterapp.services

import com.google.gson.annotations.SerializedName

data class Deal(
    var id: Int,
    var description: String,
    var merchant: String,
    @SerializedName("merchant_address")
    var merchantAddress: String
)