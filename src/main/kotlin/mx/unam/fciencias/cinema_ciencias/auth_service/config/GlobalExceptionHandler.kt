package mx.unam.fciencias.cinema_ciencias.auth_service.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Traduce los errores de autenticación a respuestas HTTP explícitas.
 *
 * Sin este handler, una [BadCredentialsException] lanzada desde el controlador llega al
 * `ExceptionTranslationFilter`, que usa el entry point por defecto de Spring Security y
 * responde 403. Credenciales inválidas son un 401 Unauthorized.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException): ResponseEntity<Map<String, Any>> =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                mapOf<String, Any>(
                    "status" to HttpStatus.UNAUTHORIZED.value(),
                    "error" to HttpStatus.UNAUTHORIZED.reasonPhrase,
                    "message" to "Credenciales inválidas"
                )
            )
}
