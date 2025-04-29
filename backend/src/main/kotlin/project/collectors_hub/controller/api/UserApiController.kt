package project.collectors_hub.controller.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
}