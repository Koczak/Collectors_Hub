package project.collectors_hub.service

import project.collectors_hub.projection.UserProjection
import project.collectors_hub.dto.UserDto
import project.collectors_hub.entity.User


interface UserService {
    fun getAllUsers(): List<UserProjection>

    fun getUserByUsername(username: String): User?

//    fun createUser(userForm: UserForm)

    fun createUser(dto: UserDto)
}