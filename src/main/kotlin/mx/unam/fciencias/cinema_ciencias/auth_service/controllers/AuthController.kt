package mx.unam.fciencias.cinema_ciencias.auth_service.controllers

import jakarta.validation.Valid
import mx.unam.fciencias.cinema_ciencias.auth_service.dto.JwtResponse
import mx.unam.fciencias.cinema_ciencias.auth_service.dto.LoginRequest
import mx.unam.fciencias.cinema_ciencias.auth_service.dto.SignUpRequest
import mx.unam.fciencias.cinema_ciencias.auth_service.entities.CinemaRole
import mx.unam.fciencias.cinema_ciencias.auth_service.entities.User
import mx.unam.fciencias.cinema_ciencias.auth_service.repositories.RoleRepository
import mx.unam.fciencias.cinema_ciencias.auth_service.security.JwtService
import mx.unam.fciencias.cinema_ciencias.auth_service.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/cinema/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository
) {

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<*> {
        if (userService.existsByUsername(signUpRequest.username)) {
            return ResponseEntity
                .badRequest()
                .body(mapOf("message" to "Error: Username is already taken!"))
        }

        if (userService.existsByEmail(signUpRequest.email)) {
            return ResponseEntity
                .badRequest()
                .body(mapOf("message" to "Error: Email is already in use!"))
        }

        val defaultRole = roleRepository.findByName(CinemaRole.ROLE_REGULAR_USER)
            .orElseThrow { RuntimeException("Error: Default role is not found in database.") }

        val user = User(
            username = signUpRequest.username ?: throw IllegalArgumentException("Username cannot be null"),
            email = signUpRequest.email ?: throw IllegalArgumentException("Email cannot be null"),
            password = passwordEncoder.encode(signUpRequest.password ?: ""),
            fullName = signUpRequest.fullName ?: "",
            enabled = true
        )

        user.roles = mutableSetOf(defaultRole)

        userService.save(user)

        return ResponseEntity.ok(mapOf("message" to "User registered successfully with ROLE_REGULAR_USER!"))
    }

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<JwtResponse> {

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        val userDetails = authentication.principal as org.springframework.security.core.userdetails.UserDetails

        val userEntity = userService.findByUsername(userDetails.username)
            ?: throw UsernameNotFoundException("User not found after authentication")

        // El Gateway valida este JWT y reenvía estos claims (X-User-Id, X-User-Roles) a los
        // demás servicios, que ya no necesitan conocer a los usuarios para identificarlos.
        val claims = mapOf<String, Any>(
            "userId" to requireNotNull(userEntity.id) { "El usuario autenticado no tiene id" },
            "roles" to userEntity.roles.map { it.name.name }
        )
        val jwt = jwtService.generateToken(claims, userDetails)

        return ResponseEntity.ok(
            JwtResponse(
                token = jwt,
                id = userEntity.id,
                username = userEntity.username,
                email = userEntity.email
            )
        )
    }
}
