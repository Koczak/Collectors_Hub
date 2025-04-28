package project.collectors_hub.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/login", "/css/**", "/js/**").permitAll() // Publiczne zasoby
                    .requestMatchers("/api/**").authenticated() // Wymagane uwierzytelnienie dla API
                    .anyRequest().authenticated() // Wymagane uwierzytelnienie dla pozostałych zasobów
            }
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            }
            .httpBasic { } // Poprawiona konfiguracja Basic Auth
            .csrf { csrf -> csrf.disable() } // Poprawiona konfiguracja CSRF
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}