package com.olivetticlub.cashregisterapp.services

interface CouponGeneration {
    fun retrieveCoupon(merchant: String, completion: (Deal?) -> Unit)
}
