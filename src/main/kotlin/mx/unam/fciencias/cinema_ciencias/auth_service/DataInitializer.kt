package mx.unam.fciencias.cinema_ciencias.auth_service

import mx.unam.fciencias.cinema_ciencias.auth_service.entities.CinemaRole
import mx.unam.fciencias.cinema_ciencias.auth_service.entities.ERole
import mx.unam.fciencias.cinema_ciencias.auth_service.repositories.RoleRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val roleRepository: RoleRepository
) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (roleRepository.findByName(CinemaRole.ROLE_REGULAR_USER).isEmpty) {
            roleRepository.save(ERole(name = CinemaRole.ROLE_REGULAR_USER))
        }
        if (roleRepository.findByName(CinemaRole.ROLE_ADMIN).isEmpty) {
            roleRepository.save(ERole(name = CinemaRole.ROLE_ADMIN))
        }
        if (roleRepository.findByName(CinemaRole.ROLE_MEMBER_USER).isEmpty) {
            roleRepository.save(ERole(name = CinemaRole.ROLE_MEMBER_USER))
        }
    }
}
