package mx.unam.fciencias.cinema_ciencias.auth_service.controllers

import jakarta.transaction.Transactional
import jakarta.validation.Valid
import mx.unam.fciencias.cinema_ciencias.auth_service.dto.SignUpRequest
import mx.unam.fciencias.cinema_ciencias.auth_service.dto.UserResponseDTO
import mx.unam.fciencias.cinema_ciencias.auth_service.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/cinema/user")
class UserController(
    private val userService: UserService
) {

    @Transactional
    @PostMapping
    fun registerUser(@Valid @RequestBody registerDTO: SignUpRequest): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(registerDTO))
    }

    @GetMapping("/all")
    fun getAllUsers(): ResponseEntity<List<UserResponseDTO>> {
        return ResponseEntity.ok(userService.getAllUsers())
    }
}
