package com.wallet.pennyservice.service

import com.wallet.pennyservice.entity.Player
import com.wallet.pennyservice.entity.TransactionType
import com.wallet.pennyservice.entity.WalletTransaction
import com.wallet.pennyservice.repository.PlayerRepository
import com.wallet.pennyservice.repository.TransactionRepository

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import org.slf4j.LoggerFactory

@Service
class WalletService(
    private val playerRepository: PlayerRepository,
    private val transactionRepository: TransactionRepository) {

    private val log = LoggerFactory.getLogger(WalletService::class.java)

    @Transactional
    fun purchase(uid: String, pid: String, amount: BigDecimal): BigDecimal {

        log.info("Incoming Purchase request: uid={}, pid={}, amount={}", uid, pid, amount)

        val player = getPlayer(pid)

        // Check if transaction exists
        if (transactionRepository.existsById(uid)) {
            log.warn("Transaction already exists, will be ignored with id={}", uid)
            return getPlayer(pid).balance
        }

        if (player.balance < amount) {
            log.warn("Not enough money for player={}, player_balance={}, requested_sum={}",
                pid, player.balance, amount
            )
            throw ResponseStatusException(
                HttpStatus.PAYMENT_REQUIRED, // or BAD_REQUEST
                "Not enough money"
            )
        }

        player.balance = player.balance.subtract(amount)
        transactionRepository.save(WalletTransaction(
            uid = uid,
            pid = pid,
            type = TransactionType.PURCHASE,
            amount = amount
        ))
        log.info("Purchase successful: uid={}, newBalance={}", uid, player.balance)
        return player.balance
    }


    @Transactional
    fun win(uid: String, pid: String, amount: BigDecimal): BigDecimal {

        log.info("Incoming Win request for player_id={}, amount={}", pid, amount)

        val player = getPlayer(pid)

        // Check for transaction exists
        if (transactionRepository.existsById(uid)) {
            log.warn("Transaction already exists, will be ignored with id={}", uid)
            return getPlayer(pid).balance
        }

        player.balance = player.balance.add(amount)
        transactionRepository.save(WalletTransaction(
            uid = uid,
            pid = pid,
            type = TransactionType.WIN,
            amount = amount
        ))
        log.info("Win successful with id={}, newBalance={}", uid, player.balance)
        return player.balance
    }


    private fun getPlayer(pid: String): Player {
        return playerRepository.findById(pid)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Player not found"
                )
            }
    }
}
