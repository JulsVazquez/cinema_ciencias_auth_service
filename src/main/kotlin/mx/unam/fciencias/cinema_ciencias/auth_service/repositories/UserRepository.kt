package mx.unam.fciencias.cinema_ciencias.auth_service.repositories

import mx.unam.fciencias.cinema_ciencias.auth_service.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
    fun existsByEmail(email: String): Boolean
    fun existsByUsername(username: String): Boolean
}