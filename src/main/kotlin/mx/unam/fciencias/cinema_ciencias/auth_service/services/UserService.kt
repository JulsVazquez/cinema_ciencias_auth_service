package mx.unam.fciencias.cinema_ciencias.auth_service.services

import mx.unam.fciencias.cinema_ciencias.auth_service.exceptions.ResourceNotFoundException
import mx.unam.fciencias.cinema_ciencias.auth_service.dto.SignUpRequest
import mx.unam.fciencias.cinema_ciencias.auth_service.dto.UserResponseDTO
import mx.unam.fciencias.cinema_ciencias.auth_service.entities.User
import mx.unam.fciencias.cinema_ciencias.auth_service.repositories.RoleRepository
import mx.unam.fciencias.cinema_ciencias.auth_service.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun registerUser(dto: SignUpRequest): UserResponseDTO {
        val user = User(
            username = dto.username ?: throw IllegalArgumentException("Username is required"),
            password = encodePassword(dto.password ?: ""),
            email = dto.email ?: throw IllegalArgumentException("Email is required"),
            fullName = dto.fullName ?: "",
            enabled = true
        )
        return UserResponseDTO(userRepository.save(user))
    }
    
    fun getUserById(id: Long): UserResponseDTO {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found") }
        return UserResponseDTO(user)
    }

    fun findByUsername(username: String): User? =
        userRepository.findByUsername(username).orElse(null)

    fun existsByUsername(username: String): Boolean =
        userRepository.existsByUsername(username)

    fun existsByEmail(email: String): Boolean =
        userRepository.existsByEmail(email)

    fun getAllUsers(): List<UserResponseDTO> {
        return userRepository.findAll()
            .map { user -> UserResponseDTO(user) }
    }

    @Transactional
    fun save(user: User): User = userRepository.save(user)

    private fun encodePassword(rawPassword: String): String {
        return passwordEncoder.encode(rawPassword) ?: throw IllegalStateException("Password encoding failed")
    }
}
