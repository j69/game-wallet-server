package com.wallet.pennyservice.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "players")
data class Player(
    @Id
    val pid: String,
    val name: String,
    var balance: BigDecimal
)
