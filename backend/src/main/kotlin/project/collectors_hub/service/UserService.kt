package project.collectors_hub.service

import project.collectors_hub.dto.GetAllUsersProjection
import project.collectors_hub.dto.UserForm


interface UserService {
    fun getAllUsers(): List<GetAllUsersProjection>

    fun createUser(userForm: UserForm)
}