package me.bpxdevjr.notes.api.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordHasher {
    private val bcrypt = BCryptPasswordEncoder()

    fun hash(password: String): String = requireNotNull(bcrypt.encode(password))

    fun verify(password: String, hash: String): Boolean = bcrypt.matches(password, hash)
}