package mx.unam.fciencias.cinema_ciencias.auth_service.entities

import jakarta.persistence.*

enum class CinemaRole {
    ROLE_ADMIN,
    ROLE_REGULAR_USER,
    ROLE_MEMBER_USER
}

@Entity
@Table(name = "roles")
class ERole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false, unique = true)
    var name: CinemaRole
)
