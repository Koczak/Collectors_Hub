package project.collectors_hub.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
class InMemoryUserConfig {

//    @Bean
//    fun userDetailsService(passwordEncoder: PasswordEncoder): UserDetailsService {
//        val user = User.builder()
//            .username("admin")
//            .password(passwordEncoder.encode("admin"))
//            .roles("ADMIN")
//            .build()
//
//        return InMemoryUserDetailsManager(user)
//    }
}