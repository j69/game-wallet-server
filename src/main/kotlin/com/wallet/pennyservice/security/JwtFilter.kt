package com.wallet.pennyservice.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@Component
class JwtFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        //println("header = $header")
        if (header == null || !header.startsWith("Bearer ")) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        val token = header.substring(7)

        if (!jwtService.validateToken(token)) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        val authentication = UsernamePasswordAuthenticationToken(
            "game-engine",
            null,
            emptyList()
        )
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.requestURI == "/api/login" // for first time to get Authtoken
    }
}
