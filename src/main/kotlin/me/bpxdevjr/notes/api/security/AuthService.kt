package me.bpxdevjr.notes.api.security

import me.bpxdevjr.notes.api.model.User
import me.bpxdevjr.notes.api.repository.RefreshTokenRepository
import me.bpxdevjr.notes.api.repository.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service

@Service
class AuthService (
    private val jwtService: JWTService,
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )

    fun register(email: String, password: String): User {
        return userRepository.save(
            User(
                email = email,
                passwordHash = passwordHasher.hash(password)
            )
        )
    }

    fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email)
            ?: throw BadCredentialsException("Invalid Credentials")

        if (!passwordHasher.verify(password, user.passwordHash)) {
            throw BadCredentialsException("Invalid Credentials")
        }

        val newAccessToken = jwtService.generateAccessToken(user.id.toString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toString())

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }
}