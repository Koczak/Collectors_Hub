package project.collectors_hub.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import project.collectors_hub.repository.UserRepository

@Service
class CollectorsHubUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username).orElseThrow { UsernameNotFoundException("User $username not found") }


        return org.springframework.security.core.userdetails.User.builder()
            .username(user.username)
            .password(user.password)
            .roles(*user.roles.split(",").toTypedArray())
            .build()
    }
}