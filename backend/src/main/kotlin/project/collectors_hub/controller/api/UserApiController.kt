package project.collectors_hub.controller.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.collectors_hub.projection.UserProjection
import project.collectors_hub.dto.UserDto
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