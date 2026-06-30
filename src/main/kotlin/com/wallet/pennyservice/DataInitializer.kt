package com.wallet.pennyservice

import com.wallet.pennyservice.entity.Player
import com.wallet.pennyservice.repository.PlayerRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class DataInitializer(
    private val playerRepository: PlayerRepository
) {
    //This one Creates Test Player with 10 euro =)
    @PostConstruct
    fun init() {
        if (!playerRepository.existsById("player1")) {
            playerRepository.save(
                Player(
                    pid = "player1",
                    name = "Jimmi",
                    balance = BigDecimal("10.00")
                )
            )
        }
    }
}
