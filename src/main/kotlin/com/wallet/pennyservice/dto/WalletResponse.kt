package com.wallet.pennyservice.dto

import java.math.BigDecimal

data class WalletResponse(
    val status: String, //TODO can be removed due to HTTP status code
    val balance: BigDecimal
)
