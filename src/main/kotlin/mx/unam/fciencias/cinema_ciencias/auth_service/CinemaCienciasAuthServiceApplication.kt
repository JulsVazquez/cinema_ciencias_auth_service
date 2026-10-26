package mx.unam.fciencias.cinema_ciencias.auth_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class CinemaCienciasAuthServiceApplication

fun main(args: Array<String>) {
	runApplication<CinemaCienciasAuthServiceApplication>(*args)
}
