package com.example.acl.headers

object ErrHeaders {
    private const val KEY = "S-Err-Action"

    val triggerOTPHeader = mapOf(
        KEY to setOf("trigger-otp-input")
    )
}