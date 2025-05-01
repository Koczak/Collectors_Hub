package project.collectors_hub.security

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import project.collectors_hub.entity.User
import project.collectors_hub.repository.UserRepository

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun init(): CommandLineRunner {
        return CommandLineRunner {
            if (userRepository.findByUsername("admin").isEmpty) {
                val user = User(
                    username = "admin",
                    password = passwordEncoder.encode("admin"),
                    email = "admin@gmail.com",
                    roles = User.ROLE_ADMIN,
                    collections = emptyList(),
                    categories = emptyList()
                )
                userRepository.save(user)
            }
        }
    }
}