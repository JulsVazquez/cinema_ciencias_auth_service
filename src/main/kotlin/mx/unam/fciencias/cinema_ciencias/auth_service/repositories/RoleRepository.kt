package mx.unam.fciencias.cinema_ciencias.auth_service.repositories

import mx.unam.fciencias.cinema_ciencias.auth_service.entities.CinemaRole
import mx.unam.fciencias.cinema_ciencias.auth_service.entities.ERole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RoleRepository : JpaRepository<ERole, Long> {
    fun findByName(name: CinemaRole): Optional<ERole>
}