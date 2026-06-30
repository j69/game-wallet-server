package com.wallet.pennyservice.repository

import com.wallet.pennyservice.entity.Player
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerRepository : JpaRepository<Player, String>
