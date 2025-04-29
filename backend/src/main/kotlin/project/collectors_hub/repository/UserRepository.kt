package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import project.collectors_hub.projection.UserProjection
import project.collectors_hub.entity.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?

    @Query("SELECT username, email FROM Users", nativeQuery = true)
    fun getAllUsers(): List<UserProjection>
}