package project.collectors_hub.security

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import project.collectors_hub.entities.User
import project.collectors_hub.repository.UserRepository

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun init(): CommandLineRunner {
        return CommandLineRunner {
            if (userRepository.findByUsername("admin") == null) {
                val user = User(
                    username = "admin",
                    password = passwordEncoder.encode("admin"),
                    roles = "ADMIN"
                )
                userRepository.save(user)
            }
        }
    }
}