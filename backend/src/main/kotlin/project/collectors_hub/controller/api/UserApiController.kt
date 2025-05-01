package project.collectors_hub.controller.api

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
    fun getUsers(): List<UserProjection> {
        return userService.getAllUsers()
    }

    @PostMapping
    fun createUser(@RequestBody dto: UserDto) {
        userService.createUser(dto)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): Boolean {
        return userService.deleteUser(id)
    }

    @PutMapping("/{id}")
    fun editUser(@PathVariable id: Long, @RequestBody dto: UserDto): Boolean {
        return userService.editUser(id, dto)
    }
}