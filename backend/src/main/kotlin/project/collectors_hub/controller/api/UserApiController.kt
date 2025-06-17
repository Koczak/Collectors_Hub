package project.collectors_hub.controller.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.collectors_hub.projection.UserProjection
import project.collectors_hub.dto.UserDto
import project.collectors_hub.projection.UserFriendProjection
import project.collectors_hub.service.UserService

@RestController
@RequestMapping("/api/users")
class UserApiController(
    private val userService: UserService
) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserProjection>> {
        return ResponseEntity(userService.getAllUsers(), HttpStatus.OK)
    }

    @GetMapping("/profile")
    fun getCurrentUserProfile(): ResponseEntity<UserProjection> {
        val currentUser = userService.getCurrentUser().orElseThrow { RuntimeException("User not found") }
        // Tworzymy obiekt UserProjection na podstawie obiektu User
        val userProjection = object : UserProjection {
            override fun getUsername(): String = currentUser.username
            override fun getEmail(): String = currentUser.email
            override fun getRoles(): String = currentUser.roles
        }
        return ResponseEntity(userProjection, HttpStatus.OK)
    }

    @PostMapping
    fun createUser(@RequestBody dto: UserDto): ResponseEntity<Unit> {
        userService.createUser(dto)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PutMapping("/{id}")
    fun editUser(@PathVariable id: Long, @RequestBody dto: UserDto): ResponseEntity<Unit> {
        userService.editUser(id, dto)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}