package com.wallet.pennyservice.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

    //Some secrets here TODO we can move secret to ENV file
    private val secret: SecretKey = Keys.hmacShaKeyFor(
        "A penny saved is a penny earned with Supercalifragilisticexpialidocious".toByteArray()
    )

    fun generateToken(): String {
        return Jwts.builder()
            .subject("game-engine")
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 60))// for 1 hour
            .signWith(secret)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}
