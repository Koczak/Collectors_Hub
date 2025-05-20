package project.collectors_hub.controller.api

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import project.collectors_hub.security.JwtTokenProvider

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @GetMapping("/status")
    fun checkAuthStatus(): ResponseEntity<Map<String, Any>> {
        val auth = SecurityContextHolder.getContext().authentication
        val isAuthenticated = auth != null && auth.isAuthenticated && auth.name != "anonymousUser"
        val username = if (isAuthenticated) auth.name else ""
        
        return ResponseEntity.ok(mapOf(
            "isAuthenticated" to isAuthenticated,
            "username" to username
        ))
    }

    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String, response: HttpServletResponse): ResponseEntity<Map<String, String>> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password)
        )
        val token = jwtTokenProvider.createToken(authentication.name)
        
        val cookie = Cookie("jwt", token)
        cookie.isHttpOnly = true
        // cookie.secure = true // Only for HTTPS - disabled in dev environment
        cookie.path = "/"
        cookie.maxAge = 3600 // 1 hour
        response.addCookie(cookie)
        
        return ResponseEntity(mapOf("message" to "Logowanie udane"), HttpStatus.OK)
    }
    
    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Map<String, String>> {
        val cookie = Cookie("jwt", "")
        cookie.isHttpOnly = true
        // cookie.secure = true // Only for HTTPS - disabled in dev environment
        cookie.path = "/"
        cookie.maxAge = 0
        response.addCookie(cookie)
        
        return ResponseEntity(mapOf("message" to "Wylogowano pomyślnie"), HttpStatus.OK)
    }
}