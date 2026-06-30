package com.wallet.pennyservice.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class WalletRequest(
    @field:NotBlank
    val uid: String,

    @field:NotBlank
    val pid: String,

    @field:DecimalMin(
        value = "2.0", //2 euro min play
        inclusive = true
    )
    val sum: BigDecimal
)
