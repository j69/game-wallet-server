package com.wallet.pennyservice.repository

import com.wallet.pennyservice.entity.WalletTransaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<WalletTransaction, String>
