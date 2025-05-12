package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import project.collectors_hub.projection.UserProjection
import project.collectors_hub.entity.User
import project.collectors_hub.projection.UserFriendProjection
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>

    @Query("SELECT username, email FROM Users", nativeQuery = true)
    fun getAllUsers(): List<UserProjection>

    @Query("""
    SELECT
        f.username AS friendUsername, 
        f.email AS friendEmail
    FROM Friend fr
    JOIN fr.friend f
    WHERE fr.user.id = :userId AND fr.status = 'ACCEPTED'
    """
    )
    fun getAllFriendsForGivenUserId(userId: Long): List<UserFriendProjection>

}