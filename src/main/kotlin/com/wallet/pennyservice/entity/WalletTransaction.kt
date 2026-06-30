package com.wallet.pennyservice.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "wallet_transactions")
data class WalletTransaction(
    @Id
    val uid: String,
    val pid: String,
    @Enumerated(EnumType.STRING)
    val type: TransactionType,
    val amount: BigDecimal,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
