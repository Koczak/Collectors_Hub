package project.collectors_hub.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.http.HttpMethod

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtTokenFilter: JwtTokenFilter): SecurityFilterChain {
        http
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/api/auth/login", "/api/auth/logout", "/api/auth/status", "/public/**", "/api/friends/confirm/**").permitAll() // Publiczne endpointy
                    .requestMatchers("/api/users").permitAll()
                    .requestMatchers("/api/**").authenticated() // Endpointy wymagające uwierzytelnienia
                    .requestMatchers(HttpMethod.GET, "/api/friends").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/friends/pending").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/friends/invite/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/friends/confirm/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/friends/reject/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/friends/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/friends/collections/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/comments/item/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/comments/collection/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/comments").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/comments/**").authenticated()
                    .anyRequest().permitAll()
            }
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java) // Dodanie filtra JWT
            .csrf { csrf -> csrf.disable() }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:4200") 
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}