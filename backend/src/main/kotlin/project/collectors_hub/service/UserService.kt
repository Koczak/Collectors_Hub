package project.collectors_hub.service

import org.springframework.stereotype.Service
import project.collectors_hub.entity.User


interface UserService {
    fun getAllUsers(): List<User>
}