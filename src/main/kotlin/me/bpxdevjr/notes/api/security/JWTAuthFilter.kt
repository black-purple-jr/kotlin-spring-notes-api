package me.bpxdevjr.notes.api.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JWTAuthFilter(private val jwtService: JWTService) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")?.trim()
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val valid = jwtService.validateAccessToken(authHeader)
            println("JWT valid: $valid")

            if (valid) {
                val userId = jwtService.getUserIdFromJWT(authHeader)

                println("JWT Subject: $userId")
                println("JWT Subject length: ${userId.length}")

                val auth = UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    emptyList()
                )

                SecurityContextHolder.getContext().authentication = auth

                println("Principal: ${SecurityContextHolder.getContext().authentication?.principal}")
            }
        }

        filterChain.doFilter(request, response)
    }
}