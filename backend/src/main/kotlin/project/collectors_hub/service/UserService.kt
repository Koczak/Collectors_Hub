package project.collectors_hub.service

import project.collectors_hub.projection.UserProjection
import project.collectors_hub.dto.UserDto
import project.collectors_hub.entity.User
import project.collectors_hub.projection.UserFriendProjection
import java.util.Optional


interface UserService {
    fun getAllUsers(): List<UserProjection>

    fun getCurrentUser(): Optional<User>

    fun getUserByUsername(username: String): Optional<User>

//    fun createUser(userForm: UserForm)

    fun createUser(dto: UserDto)

    fun deleteUser(id: Long): Boolean

    fun editUser(id: Long, dto: UserDto): Boolean

    fun getAllFriendsForCurrentUser(): List<UserFriendProjection>
}