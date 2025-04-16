package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import project.collectors_hub.entity.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}