package com.wallet.pennyservice.controller

import org.springframework.web.bind.annotation.*
import com.wallet.pennyservice.dto.WalletResponse
import com.wallet.pennyservice.dto.WalletRequest
import com.wallet.pennyservice.service.WalletService
import jakarta.validation.Valid

@RestController
@RequestMapping("/api")
class WalletController(
    private val walletService: WalletService
) {

    @PostMapping("/purchase")
    fun purchase(@Valid @RequestBody request: WalletRequest): WalletResponse {
        val balance = walletService.purchase(
            uid = request.uid,
            pid = request.pid,
            amount = request.sum
        )

        return WalletResponse("success", balance)
    }

    @PostMapping("/win")
    fun win(@RequestBody request: WalletRequest): WalletResponse {
        val balance = walletService.win(
            uid = request.uid,
            pid = request.pid,
            amount = request.sum
        )

        return WalletResponse("success", balance)
    }
}
