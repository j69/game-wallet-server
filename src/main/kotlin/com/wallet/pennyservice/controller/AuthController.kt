package com.wallet.pennyservice.controller

import org.springframework.web.bind.annotation.*
import com.wallet.pennyservice.dto.LoginResponse
import com.wallet.pennyservice.dto.LoginRequest
import com.wallet.pennyservice.security.JwtService
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/api")
class AuthController(
    private val jwtService: JwtService
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse {
        // TODO we can move clientId and secret to ENV file
        if (request.clientId != "VeikGameEngine" || request.secret != "blackjack") {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid credentials"
            )
        }
        val token = jwtService.generateToken()
        return LoginResponse(token)
    }

}
