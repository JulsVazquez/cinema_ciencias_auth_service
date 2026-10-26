package mx.unam.fciencias.cinema_ciencias.auth_service.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import mx.unam.fciencias.cinema_ciencias.auth_service.entities.User
import java.time.LocalDateTime

data class UserRegistroDTO(
    @field:NotBlank(message = "El nombre de usuario es obligatorio")
    @field:Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    val username: String,

    @field:NotBlank(message = "El email es obligatorio")
    @field:Email(message = "El formato del email no es válido")
    val email: String,

    @field:NotBlank(message = "La contraseña es obligatoria")
    @field:Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    val password: String
)

data class UserRequestDTO(
    @field:NotBlank(message = "El nombre de usuario es obligatorio")
    @field:Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    val username: String,

    @field:NotBlank(message = "El email es obligatorio")
    @field:Email(message = "El formato del email no es válido")
    val email: String,

    @field:NotBlank(message = "La contraseña es obligatoria")
    @field:Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    val password: String
)

data class UserResponseDTO(
    val userId: Long?,
    val username: String,
    val email: String,
    val fullName: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
    constructor(user: User) : this(
        userId = user.id,
        username = user.username,
        email = user.email,
        fullName = user.fullName,
        createdAt = user.createdAt,
        updatedAt = user.updatedAt
    )
}

data class LoginRequest(
    @field:NotBlank val username: String,
    @field:NotBlank val password: String
)

data class JwtResponse(
    val token: String,
    val id: Long?,
    val username: String,
    val email: String,
    val type: String = "Bearer"
)

data class SignUpRequest(
    @field:NotBlank(message = "username is required")
    @field:Size(min = 3, max = 50)
    val username: String,
    
    @field:NotBlank(message = "email is required")
    @field:Email
    val email: String,
    
    @field:NotBlank(message = "password is required")
    @field:Size(min = 6)
    val password: String,
    
    @field:NotBlank(message = "fullName is required")
    @field:Size(min = 3, max = 100)
    val fullName: String? = null
)
